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

// Verify that the user is a professor (perm_level 3)
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

// Check if form data is posted
if (isset($_POST['db']['existing']) && is_array($_POST['db']['existing'])) {

    foreach ($_POST['db']['existing'] as $db_id => $data) {
        // Clean input values
        $db_id = (int)$data['db_id'];
        $posted_team_id = isset($data['team_id']) ? trim($data['team_id']) : "";
        $posted_db_name = isset($data['db_name']) ? trim($data['db_name']) : "";
        $posted_db_description = isset($data['db_description']) ? trim($data['db_description']) : "";
        $deleteFlag = isset($data['delete']) ? strtoupper(trim($data['delete'])) : "";

        // If delete marker is "Y", delete this entry and skip update comparison
        if ($deleteFlag === "Y") {
            $delStmt = $conn->prepare("DELETE FROM db WHERE db_id = ?");
            $delStmt->bind_param("i", $db_id);
            $delStmt->execute();
            $delStmt->close();
            continue;
        }

        // Retrieve the current values for comparison.
        $selectStmt = $conn->prepare("SELECT team_id, db_name, db_description FROM db WHERE db_id = ?");
        $selectStmt->bind_param("i", $db_id);
        $selectStmt->execute();
        $resultRow = $selectStmt->get_result();
        if ($resultRow->num_rows === 0) {
            // No record found; move to next.
            $selectStmt->close();
            continue;
        }
        $current = $resultRow->fetch_assoc();
        $selectStmt->close();

        // Note: Database ID and Team ID are not allowed to change.
        // Even if posted_team_id does not match current team_id, ignore changes.
        // Allowed to update db_name and db_description.
        if ($posted_db_name !== $current['db_name'] || $posted_db_description !== $current['db_description']) {
            // Generate a single timestamp for both the update and the changelog.
            $current_time = date("Y-m-d H:i:s");

            // Update the record using the same timestamp.
            $updateStmt = $conn->prepare("UPDATE db SET db_name = ?, db_description = ?, last_updated = ? WHERE db_id = ?");
            $updateStmt->bind_param("sssi", $posted_db_name, $posted_db_description, $current_time, $db_id);
            $updateStmt->execute();
            $updateStmt->close();
            
            // Limit snippets to 200 characters.
            $originalName = isset($current['db_name']) ? substr($current['db_name'], 0, 200) : "";
            $originalDescription = isset($current['db_description']) ? substr($current['db_description'], 0, 200) : "";
            $newName = substr($posted_db_name, 0, 200);
            $newDescription = substr($posted_db_description, 0, 200);

            // Construct the change description.
            $changeDescription = "Updated db_name: from '{$originalName}' to '{$newName}'. Updated db_description: from '{$originalDescription}' to '{$newDescription}'.";

            // Insert an entry in the changelog with the same timestamp.
            $logStmt = $conn->prepare("INSERT INTO changelog (username, db_id, change_description, time_of_change) VALUES (?, ?, ?, ?)");
            $logStmt->bind_param("siss", $loggedUser, $db_id, $changeDescription, $current_time);
            $logStmt->execute();
            $logStmt->close();
        }
    }
}

$conn->close();
header("Location: prof.php");
exit;
?>