<?php
session_start(); // Start session to access logged-in user

// Set current user if logged in, otherwise default to an empty string.
$loggedUser = isset($_SESSION['username']) ? $_SESSION['username'] : "";

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

// Get the database ID from the URL.
$db_id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);
if (!$db_id) {
  exit("Database ID is missing or invalid.");
}

// Updated SQL: fetch both team user IDs and display names
$sql = "SELECT db.db_name, db.db_description, db.last_updated, 
       t.user1, t.user2, t.user3,
       CONCAT(u1.fname, ' ', u1.lname) AS user1_name,
       CONCAT(u2.fname, ' ', u2.lname) AS user2_name,
       CONCAT(u3.fname, ' ', u3.lname) AS user3_name,
       g.grade
    FROM db
    JOIN team t ON db.team_id = t.team_id
    LEFT JOIN users u1 ON t.user1 = u1.username
    LEFT JOIN users u2 ON t.user2 = u2.username
    LEFT JOIN users u3 ON t.user3 = u3.username
    LEFT JOIN grades g ON db.db_id = g.db_id
    WHERE db.db_id = ?";

$statement = $conn->prepare($sql);
if (!$statement) {
  exit("SQL error: " . $conn->error);
}
$statement->bind_param("i", $db_id);
$statement->execute();
$result = $statement->get_result();
$database = $result->fetch_assoc();

if (!$database) {
  exit("No database found with the given ID.");
}

// Check if the current user is part of the database team
$is_team_member = (
  $loggedUser === $database['user1'] ||
  $loggedUser === $database['user2'] ||
  $loggedUser === $database['user3']
);

// Check user permissions for viewing the changelog
$perm_sql = "SELECT perm_level FROM permissions WHERE username = ?";
$perm_statement = $conn->prepare($perm_sql);
$perm_statement->bind_param("s", $loggedUser);
$perm_statement->execute();
$perm_result = $perm_statement->get_result();
$permissions = $perm_result->fetch_assoc();
$can_view_changelog = $permissions && 
  (
    ($permissions['perm_level'] == 2 || $permissions['perm_level'] == 3) ||
    ($permissions['perm_level'] == 1 && $is_team_member)
  );
?>

<!DOCTYPE html>
<html lang="en">
  <head>
  <meta charset="utf-8"/>
  <title>Database Details | The Databasebase</title>
  <link rel="stylesheet" href="style.css"/>
  </head>
  <body>
  <header>
    <h1><a href="index.php">The Databasebase</a></h1>
  </header>
  <main>
  <!-- Display database details -->
  <div class="database-card">
    <h2><?php echo htmlspecialchars($database['db_name']); ?></h2>
    <p><?php echo htmlspecialchars($database['db_description']); ?></p>
    <p>Team: 
    <?php 
      echo ($loggedUser === $database['user1'])
        ? '<strong>' . htmlspecialchars($database['user1_name']) . '</strong>'
        : htmlspecialchars($database['user1_name']);
      if (!empty($database['user2_name'])) {
        echo ", ";
        echo ($loggedUser === $database['user2'])
          ? '<strong>' . htmlspecialchars($database['user2_name']) . '</strong>'
          : htmlspecialchars($database['user2_name']);
      }
      if (!empty($database['user3_name'])) {
        echo ", ";
        echo ($loggedUser === $database['user3'])
          ? '<strong>' . htmlspecialchars($database['user3_name']) . '</strong>'
          : htmlspecialchars($database['user3_name']);
      }
    ?>
    </p>
    <p>Grade: <?php echo isset($database['grade']) ? htmlspecialchars($database['grade']) : "Not Graded"; ?></p>
    <p>Last updated: <?php echo htmlspecialchars($database['last_updated']); ?></p>
  </div>

  <div style="text-align: center;">
    <!-- Allow team members and permission level 3 to edit -->
    <?php if ($is_team_member || ($permissions && $permissions['perm_level'] == 3)): ?>
      <button onclick="location.href='dbedit.php?id=<?php echo htmlspecialchars($db_id); ?>'"><strong>Edit Database Info</strong></button>
    <?php endif; ?>

    <!-- Display a button to view the changelog if the user has sufficient permissions -->
    <?php if ($can_view_changelog): ?>
      <button onclick="location.href='changes.php?id=<?php echo htmlspecialchars($db_id); ?>'"><strong>View Changelog</strong></button>
    <?php endif; ?>
  </div>

  </main>
  </body>
</html>
