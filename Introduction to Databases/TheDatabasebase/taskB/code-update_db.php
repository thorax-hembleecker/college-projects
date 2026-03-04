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

// Create connection to the MySQL database
$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
  exit("Connection failed: " . $conn->connect_error);
}

// Get the database ID from the URL.
$db_id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);
if (!$db_id) {
  exit("Database ID is missing or invalid.");
}

// Fetch the user's permission level.
$permStmt = $conn->prepare("SELECT perm_level FROM permissions WHERE username = ?");
$permStmt->bind_param("s", $loggedUser);
$permStmt->execute();
$permResult = $permStmt->get_result();
if ($permRow = $permResult->fetch_assoc()) {
    $user_perm = (int)$permRow['perm_level'];
} else {
    header("Location: index.php");
    exit;
}
$permStmt->close();

// If the user is not a professor (perm_level 3), verify team membership.
if ($user_perm !== 3) {
    if ($user_perm !== 1) {
        header("Location: index.php");
        exit;
    }
    $teamStmt = $conn->prepare("SELECT team.user1, team.user2, team.user3 
            FROM team 
            JOIN db ON team.team_id = db.team_id 
            WHERE db.db_id = ?");
    $teamStmt->bind_param("i", $db_id);
    $teamStmt->execute();
    $teamResult = $teamStmt->get_result();
    if ($teamRow = $teamResult->fetch_assoc()) {
        $teamMembersDB = array_filter([$teamRow['user1'], $teamRow['user2'], $teamRow['user3']]);
        if (!in_array($loggedUser, $teamMembersDB)) {
            header("Location: index.php");
            exit;
        }
    } else {
        header("Location: index.php");
        exit;
    }
    $teamStmt->close();
}

// Process the form submission.
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve and trim form data.
    $dbName = trim($_POST['dbName'] ?? '');
    $description = trim($_POST['description'] ?? '');

    // Validate required fields.
    if (empty($dbName) || empty($description)) {
        exit("Database name and description must not be empty.");
    }

    // Capture the current date and time
    $currentDateTime = date("Y-m-d H:i:s");

    // Retrieve the current DB values before the update.
    $selectStmt = $conn->prepare("SELECT db_name, db_description FROM db WHERE db_id = ?");
    $selectStmt->bind_param("i", $db_id);
    $selectStmt->execute();
    $selectResult = $selectStmt->get_result();
    $original = $selectResult->fetch_assoc();
    $selectStmt->close();

    // Prepare and execute the update statement.
    $updateStmt = $conn->prepare("UPDATE db SET db_name = ?, db_description = ?, last_updated = ? WHERE db_id = ?");
    $updateStmt->bind_param("sssi", $dbName, $description, $currentDateTime, $db_id);

    // Generate changelog entry.
    if ($updateStmt->execute()) {
        // Limit snippets to 200 characters.
        $originalName = isset($original['db_name']) ? substr($original['db_name'], 0, 200) : "";
        $originalDescription = isset($original['db_description']) ? substr($original['db_description'], 0, 200) : "";
        $newName = substr($dbName, 0, 200);
        $newDescription = substr($description, 0, 200);

        // Construct the change description.
        $changeDescription = "Updated db_name: from '{$originalName}' to '{$newName}'. Updated db_description: from '{$originalDescription}' to '{$newDescription}'.";

        // Insert a record into changelog.
        $logStmt = $conn->prepare("INSERT INTO changelog (username, db_id, change_description, time_of_change) VALUES (?, ?, ?, ?)");
        $logStmt->bind_param("siss", $loggedUser, $db_id, $changeDescription, $currentDateTime);
        $logStmt->execute();
        $logStmt->close();

        // Redirect to home page after successful update.
        header("Location: details.php?id=" . $db_id);
        exit;
    } else {
        echo "Error updating database: " . $conn->error;
    }
    $updateStmt->close();
}

$conn->close();
?>