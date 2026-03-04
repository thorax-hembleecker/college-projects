<?php
// Start session and require login.
session_start();
if (!empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

// only accept POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
  header('Location: login.html');
  exit;
}

// grab & trim inputs
$user = trim($_POST['username']);
$pass = $_POST['password'];



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



// Look up stored hash.
$changeStmt = $conn->prepare('SELECT pw FROM users WHERE username = ?');
$changeStmt->bind_param('s', $user);
$changeStmt->execute();
$changeStmt->store_result();

if ($changeStmt->num_rows === 1) {
  $changeStmt->bind_result($db_pass);
  $changeStmt->fetch();

  // Me when I don't feel like implementing password hashing :P
  if ($pass === $db_pass) {
      $_SESSION['username'] = $user;
      header('Location: index.php');
      exit;
  }
  $error = 'Invalid password.';
} else {
  $error = 'No such user.';
}


$changeStmt->close();
$conn->close();

header('Location: login.html?error=' . urlencode($error));
exit;
?>
