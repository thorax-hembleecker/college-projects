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
  // If the connection fails, exit and display an error message!!
  exit("Connection failed: " . $conn->connect_error);
}

// Get the database ID from the URL.
$db_id = filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT);
if (!$db_id) {
  exit("Database ID is missing or invalid.");
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

// If the user is not a professor (perm_level 3) then check team membership.
if ($user_perm !== 3) {
    // Only team members (perm_level 1) are allowed.
    if ($user_perm !== 1) {
        header("Location: index.php");
        exit;
    }
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

// Now that permissions are verified, fetch the current database details.

// SQL query to fetch database details, team members, and grade information.
$sql = "SELECT db.db_name, db.db_description, grades.grade, db.last_updated, 
         CONCAT(u1.fname, ' ', u1.lname) AS member1,
         CONCAT(u2.fname, ' ', u2.lname) AS member2,
         CONCAT(u3.fname, ' ', u3.lname) AS member3
      FROM db
      LEFT JOIN team ON db.team_id = team.team_id
      LEFT JOIN users u1 ON team.user1 = u1.username
      LEFT JOIN users u2 ON team.user2 = u2.username
      LEFT JOIN users u3 ON team.user3 = u3.username
      LEFT JOIN grades ON db.db_id = grades.db_id
      WHERE db.db_id = ?";
$statement = $conn->prepare($sql);
$statement->bind_param("i", $db_id);
$statement->execute();
$result = $statement->get_result();
$row = $result->fetch_assoc();

// If no data is found, display an error and exit.
if ($row) {
  $dbName = htmlspecialchars($row['db_name']);
  $description = htmlspecialchars($row['db_description']);
  $grade = htmlspecialchars($row['grade']);
  $lastUpdated = htmlspecialchars($row['last_updated']);
  $teamMembers = array_filter([
    htmlspecialchars($row['member1']),
    htmlspecialchars($row['member2']),
    htmlspecialchars($row['member3'])
  ]);
} else {
  echo "<p>Database details not found.</p>";
  exit;
}
$statement->close();
?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8"/>
    <title>Edit Database | The Databasebase</title>
    <link rel="stylesheet" href="style.css"/>
  </head>
  <body>
    <div class="details">
      <header>
        <h1><a href="index.php">The Databasebase</a></h1>
      </header>
      <main class="dbedit">
        <h2>Edit your database details</h2>
        <form method="post" action="code-update_db.php?id=<?php echo $db_id; ?>">
          <div>
            <label for="dbName">Database Name:</label>
            <br>
            <input type="text" id="dbName" name="dbName" value="<?php echo $dbName; ?>" required/>
          </div>
          <br>
          <div>
            <label for="description">Description:</label>
            <br>
            <textarea id="description" name="description" rows="10" cols="75" required><?php echo $description; ?></textarea>
          </div>
          <div class="db-info">
            <h3>More details</h3>
            <ul>
              <li><strong>Team Members:</strong> <?php echo implode(", ", $teamMembers); ?></li>
              <li><strong>Grade:</strong> <?php echo $grade; ?></li>
              <li><strong>Last Updated:</strong> <?php echo $lastUpdated; ?></li>
            </ul>
          </div>
          <button type="submit">Save Changes</button>
        </form>
      </main>
    </div>
  </body>
</html>
