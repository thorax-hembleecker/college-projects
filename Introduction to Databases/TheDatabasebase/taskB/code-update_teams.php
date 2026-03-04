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

$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
    exit("Connection failed: " . $conn->connect_error);
}

// Verify that the user is a professor by checking perm_level.
$sql = "SELECT perm_level FROM permissions WHERE username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $loggedUser);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows === 0 || ((int)$result->fetch_assoc()['perm_level'] !== 3)) {
    header("Location: login.html");
    exit;
}
$stmt->close();

// Check if team data exists.
if (!isset($_POST['team']['existing'])) {
    header("Location: prof.php");
    exit;
}

$teams = $_POST['team']['existing'];

foreach ($teams as $team_id => $teamData) {
    // If "delete" field contains "Y", mark for deletion and skip update.
    if (isset($teamData['delete']) && strtoupper(trim($teamData['delete'])) === "Y") {
        $deleteStmt = $conn->prepare("DELETE FROM team WHERE team_id = ?");
        $deleteStmt->bind_param("i", $team_id);
        $deleteStmt->execute();
        $deleteStmt->close();
        continue;
    }

    // Get the current values from the database for comparison.
    $selectStmt = $conn->prepare("SELECT user1, user2, user3 FROM team WHERE team_id = ?");
    $selectStmt->bind_param("i", $team_id);
    $selectStmt->execute();
    $resultSelect = $selectStmt->get_result();
    if ($resultSelect->num_rows === 0) {
        $selectStmt->close();
        continue;
    }
    $currentTeam = $resultSelect->fetch_assoc();
    $selectStmt->close();

    // Gather submitted values and shift them left if empty spaces exist.
    $inputUsers = [];
    foreach (['user1', 'user2', 'user3'] as $field) {
        $value = isset($teamData[$field]) ? trim($teamData[$field]) : "";
        if ($value !== "") {
            $inputUsers[] = $value;
        }
    }
    // Pad the users to always have three elements.
    while (count($inputUsers) < 3) {
        $inputUsers[] = null;
    }
    list($new_user1, $new_user2, $new_user3) = $inputUsers;

    // Prepare current values for a fair comparison (treat empty strings as null).
    $curr_user1 = ($currentTeam['user1'] === "") ? null : $currentTeam['user1'];
    $curr_user2 = ($currentTeam['user2'] === "") ? null : $currentTeam['user2'];
    $curr_user3 = ($currentTeam['user3'] === "") ? null : $currentTeam['user3'];

    // If any user differs, update the team record.
    if ($new_user1 !== $curr_user1 || $new_user2 !== $curr_user2 || $new_user3 !== $curr_user3) {
        $updateStmt = $conn->prepare("UPDATE team SET user1 = ?, user2 = ?, user3 = ? WHERE team_id = ?");
        $updateStmt->bind_param("sssi", $new_user1, $new_user2, $new_user3, $team_id);
        $updateStmt->execute();
        $updateStmt->close();
    }

    // Check if the team is now empty (all user fields are null or empty).
    if ((empty($new_user1)) && (empty($new_user2)) && (empty($new_user3))) {
        $deleteStmt = $conn->prepare("DELETE FROM team WHERE team_id = ?");
        $deleteStmt->bind_param("i", $team_id);
        $deleteStmt->execute();
        $deleteStmt->close();
    }
}

$conn->close();
header("Location: prof.php");
exit;
?>
