<?php
// Start session and require login.
session_start();
if (empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    header("Location: index.php");
    exit();
}

$loggedUser = $_SESSION['username'];
$currentPasswordInput = trim($_POST['current_password']);
$newPassword = trim($_POST['new_password']);

// Database connection details.
$host = "localhost"; // Server name or IP address where the database is hosted.
$db_username = "example";  // Username to connect to the database.
$db_password = "password";  // Password for the database user.
$db_name = "example_dbb"; // Name of the database to connect to.

// Create connection to the MySQL database
$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
  // If the connection fails, exit and display an error message!!
  exit("Connection failed: " . $conn->connect_error);
}
// Prepare a statement to fetch the current password for the user
$stmt = $conn->prepare("SELECT pw FROM users WHERE username = ?");
$stmt->bind_param("s", $loggedUser);
$stmt->execute();
$stmt->store_result();

if ($stmt->num_rows !== 1) {
    $stmt->close();
    $conn->close();
    die("User not found.");
}

$stmt->bind_result($passwordInDB);
$stmt->fetch();
$stmt->close();

// Compare the current password input with the one in database
if ($currentPasswordInput !== $passwordInDB) {
    $conn->close();
    die("Current password is incorrect.");
}

// Prepare a statement to update the password
$updateStmt = $conn->prepare("UPDATE users SET pw = ? WHERE username = ?");
$updateStmt->bind_param("ss", $newPassword, $loggedUser);

if ($updateStmt->execute()) {
    $updateStmt->close();
    $conn->close();
    header("Location: index.php");
    exit();
} else {
    $updateStmt->close();
    $conn->close();
    die("Error updating password.");
}
?>