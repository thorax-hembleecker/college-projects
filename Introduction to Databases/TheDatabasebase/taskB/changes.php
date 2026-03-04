<?php

// Start session and require login.
session_start();
if (empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

$loggedUser = $_SESSION['username'];

// Get the database ID from the URL.
$db_id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);
if (!$db_id) {
  exit("Database ID is missing or invalid.");
}

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

// Get the user's permission level.
$permStmt = $conn->prepare("SELECT perm_level FROM permissions WHERE username = ?");
$permStmt->bind_param("s", $loggedUser);
$permStmt->execute();
$permResult = $permStmt->get_result();
if ($permRow = $permResult->fetch_assoc()) {
  $user_perm = (int)$permRow['perm_level'];
} else {
  // If user not found in permissions, redirect.
  header("Location: index.php");
  exit;
}
$permStmt->close();

// If the user is not a TA or professor (perm_level 2 or 3) then check team membership.
if ($user_perm !== 3 && $user_perm !== 2) {
    // Query to fetch the team members for the database.
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
        // If the team is not found, deny access.
        header("Location: index.php");
        exit;
    }
    $teamStmt->close();
}

// Query to fetch changes from the changelog table
$statement = $conn->prepare("SELECT change_description, time_of_change, username FROM `changelog` WHERE db_id=? ORDER BY time_of_change DESC");
if (!$statement) {
    exit("Prepare failed" . $conn->error);
}
$statement->bind_param("i", $db_id);
$statement->execute();
$result = $statement->get_result();

$team = $conn->prepare("SELECT team FROM `db` WHERE db_id = ?");

?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <title>Database Changes | The Databasebase</title>
        <link rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <header>
            <h1><a href="index.php">The Databasebase</a></h1>
        </header>
        <main>
            <!-- Dynamically load database changes from the 'change' table -->
            <div class="changes-list">
                <h2>Database Changes</h2>
                <?php
                // Check if there are results and display them
                if ($result && $result->num_rows > 0) {
                    while ($row = $result->fetch_assoc()) {
                        echo '<div class="change-box">';
                        echo '<strong>Change:</strong> ' . htmlspecialchars($row['change_description']) . '<br/>';
                        echo '<strong>Made by:</strong> '. htmlspecialchars($row['username']) . '<br/>';
                        echo '<strong>Date:</strong> ' . htmlspecialchars($row['time_of_change']) . '</div>';
                    }
                } else {
                    echo '<p>No changes found in the database.</p>';
                }
                ?>
            </div>
        </main>
    </body>
</html>