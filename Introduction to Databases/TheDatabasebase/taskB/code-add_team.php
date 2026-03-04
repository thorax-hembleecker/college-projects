<?php
// Start session and require login.
session_start();
if (empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

$loggedUser = $_SESSION['username'];

// Database connection details.
$host = "localhost";
$db_username = "example";
$db_password = "password";
$db_name = "example_dbb";

// Create connection.
$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
    exit("Connection failed: " . $conn->connect_error);
}

// Verify that the user is a professor.
$sql = "SELECT perm_level FROM permissions WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $loggedUser);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows === 0) {
    header("Location: login.html");
    exit;
}
$row = $result->fetch_assoc();
if ((int)$row['perm_level'] !== 3) {
    header("Location: login.html");
    exit;
}
$stmt->close();

// Check for proper POST submission for team data.
if (!isset($_POST['team']['id'], $_POST['team']['new']) || !is_array($_POST['team']['new'])) {
    header("Location: prof.php");
    exit;
}

// Validate and assign team ID. It must be numeric.
$team_id_input = trim($_POST['team']['id']);
if (!ctype_digit($team_id_input)) {
    header("Location: prof.php");
    exit;
}
$team_id = (int)$team_id_input;

// Function to verify the existence of a user and ensure they are not already assigned to a team.
function isValidUser($conn, $username)
{
    // Check if the username exists in the users table.
    $stmt = $conn->prepare("SELECT username FROM users WHERE username = ?");
    if (!$stmt) {
        return false;
    }
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows === 0) {
        $stmt->close();
        return false;
    }
    $stmt->close();

    // Check if the user is already assigned to any team.
    $stmt2 = $conn->prepare("SELECT team_id FROM team WHERE user1 = ? OR user2 = ? OR user3 = ?");
    if (!$stmt2) {
        return false;
    }
    $stmt2->bind_param("sss", $username, $username, $username);
    $stmt2->execute();
    $result2 = $stmt2->get_result();
    if ($result2->num_rows > 0) {
        $stmt2->close();
        return false;
    }
    $stmt2->close();
    return true;
}

// Apply "shift users over" logic.
// 1. Retrieve the submitted data.
$data = $_POST['team']['new'];
$shiftedUsers = array();

// 2. Trim each input and include it only if nonempty and valid.
foreach ($data as $user_input) {
    $trimmedUser = trim($user_input);
    if ($trimmedUser !== "" && isValidUser($conn, $trimmedUser)) {
        $shiftedUsers[] = $trimmedUser;
    }
}

// Ensure at least one valid user exists (user1 is required).
if (empty($shiftedUsers)) {
    header("Location: prof.php");
    exit;
}

// Pad the array so it always contains three elements.
while (count($shiftedUsers) < 3) {
    $shiftedUsers[] = null;
}
list($new_user1, $new_user2, $new_user3) = $shiftedUsers;

// Prepare and execute the INSERT statement for the new team record.
$stmtInsert = $conn->prepare("INSERT INTO team (team_id, user1, user2, user3) VALUES (?, ?, ?, ?)");
$stmtInsert->bind_param("isss", $team_id, $new_user1, $new_user2, $new_user3);
$stmtInsert->execute();
$stmtInsert->close();

$conn->close();
header("Location: prof.php");
exit;
?>
