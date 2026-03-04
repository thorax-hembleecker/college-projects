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

// Create connection
$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
    exit("Connection failed: " . $conn->connect_error);
}

// Verify the user is a professor.
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

// Get new database entry data from the add form.
if (!isset($_POST['db']['new']) || count($_POST['db']['new']) < 3) {
    exit("Invalid form submission.");
}
$newEntry = $_POST['db']['new'];
$team_id = trim($newEntry[0]);
$db_name_entry = trim($newEntry[1]);
$db_description = trim($newEntry[2]);

// Validate team_id: must be numeric.
if (!is_numeric($team_id)) {
    exit("Invalid Team ID.");
}

// Verify that the team ID exists.
$teamCheck = $conn->prepare("SELECT team_id FROM team WHERE team_id = ?");
$teamCheck->bind_param("i", $team_id);
$teamCheck->execute();
$teamResult = $teamCheck->get_result();
if ($teamResult->num_rows === 0) {
    $teamCheck->close();
    exit("Team ID does not exist. Transaction cancelled.");
}
$teamCheck->close();

// Insert new database entry into `db` table.
// db_id is auto-increment so we omit it. Use NOW() for last_updated.
$insertSQL = "INSERT INTO `db` (team_id, db_name, db_description, last_updated) VALUES (?, ?, ?, NOW())";
$insertStmt = $conn->prepare($insertSQL);
if (!$insertStmt) {
    exit("Prepare failed: " . $conn->error);
}
$insertStmt->bind_param("iss", $team_id, $db_name_entry, $db_description);
if ($insertStmt->execute()) {
    // Get the new auto-incremented db_id.
    $new_db_id = $conn->insert_id;
    $insertStmt->close();
    
    // Insert changelog entry.
    $changelogSQL = "INSERT INTO changelog (username, db_id, change_description, time_of_change) VALUES (?, ?, ?, NOW())";
    $changelogStmt = $conn->prepare($changelogSQL);
    if (!$changelogStmt) {
        exit("Prepare failed for changelog: " . $conn->error);
    }
    $description = "database created";
    $changelogStmt->bind_param("sis", $loggedUser, $new_db_id, $description);
    if (!$changelogStmt->execute()) {
        $changelogStmt->close();
        $conn->close();
        exit("Error inserting changelog entry: " . $conn->error);
    }
    $changelogStmt->close();
    
    $conn->close();
    header("Location: prof.php");
    exit;
} else {
    $insertStmt->close();
    $conn->close();
    exit("Error inserting new database entry: " . $conn->error);
}
?>