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

// Check permission: must be professor (perm_level 3)
$sql = "SELECT perm_level FROM permissions WHERE username = ?";
$statement = $conn->prepare($sql);
$statement->bind_param("s", $loggedUser);
$statement->execute();
$result = $statement->get_result();
$row = $result->fetch_assoc();
$statement->close();

if (!$row || (int)$row['perm_level'] !== 3) {
    header("Location: login.html");
    exit;
}

// Check if form was submitted with the new user data
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['users']['new'])) {
    // Retrieve new user information
    $newData = $_POST['users']['new'];

    // Expected order: username, pw, fname, lname, title, perm_level
    $newUsername = trim($newData[0]);
    $newPw       = trim($newData[1]);
    $newFname    = trim($newData[2]);
    $newLname    = trim($newData[3]);
    $newTitle    = trim($newData[4]);
    $newPerm = (int) trim($newData[5]);
    if (!in_array($newPerm, [1, 2, 3], true)) {
        exit("Invalid permission level. Must be 1, 2, or 3.");
    }

    // Begin transaction
    $conn->begin_transaction();

    try {
        // Insert new user into users table
        $userQuery = "INSERT INTO users (username, pw, fname, lname, title) VALUES (?, ?, ?, ?, ?)";
        $userStmt = $conn->prepare($userQuery);
        $userStmt->bind_param("sssss", $newUsername, $newPw, $newFname, $newLname, $newTitle);
        if (!$userStmt->execute()) {
            throw new Exception("Error adding user: " . $userStmt->error);
        }
        $userStmt->close();

        // Insert corresponding permissions record
        $permQuery = "INSERT INTO permissions (username, perm_level) VALUES (?, ?)";
        $permStmt = $conn->prepare($permQuery);
        $permStmt->bind_param("si", $newUsername, $newPerm);
        if (!$permStmt->execute()) {
            throw new Exception("Error adding permission: " . $permStmt->error);
        }
        $permStmt->close();

        // Commit transaction
        $conn->commit();
    } catch (Exception $e) {
        // Rollback transaction on error
        $conn->rollback();
        exit($e->getMessage());
    }
}

// Redirect back to the professor panel.
header("Location: prof.php");
exit;
?>