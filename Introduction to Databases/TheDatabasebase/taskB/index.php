<?php
session_start(); // Start the session to manage user login state

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
?>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8"/>
    <title>Home | The Databasebase</title>
    <link rel="stylesheet" href="style.css"/>
  </head>
  <body>
  <div class="dbhome">
    <header>
      <h1>Welcome to the Databasebase</h1> <!-- Main heading -->
    </header>
        <?php
        // Check if the user is logged in
        if (isset($_SESSION['username'])) {
          $loggedUser = $_SESSION['username']; // Get the current logged-in username

          echo '<nav>
            <ul>';

          // Query to fetch the permission level of the logged-in user
          $sql = "SELECT perm_level FROM permissions WHERE username = ?";
          $permStmt = $conn->prepare($sql); // Prepare the SQL statement
          $permStmt->bind_param("s", $loggedUser); // Bind the username parameter
          $permStmt->execute(); // Execute the query
          $result = $permStmt->get_result(); // Get the result of the query

          // Check if the user has a permission level
          if ($result->num_rows > 0) {
            $row = $result->fetch_assoc(); // Fetch the permission level
            $perm_level = $row['perm_level'];
            
            // Display additional buttons based on the user's permission level
            if ((int)$perm_level === 1) { // Student
              // Query the database ID associated with the student's team
              $dbStmt = $conn->prepare("SELECT db.db_id FROM db JOIN team ON db.team_id = team.team_id WHERE team.user1 = ? OR team.user2 = ? OR team.user3 = ?");
              $dbStmt->bind_param("sss", $loggedUser, $loggedUser, $loggedUser);
              $dbStmt->execute();
              $result_student = $dbStmt->get_result();
              if ($result_student->num_rows > 0) {
                $db_row = $result_student->fetch_assoc();
                $db_id = $db_row['db_id'];
              } else {
                $db_id = ""; // Fallback if no associated database is found
              }
              $dbStmt->close();
              
              echo '<li>
                  <form action="details.php" method="get">
                    <input type="hidden" name="id" value="' . htmlspecialchars($db_id) . '"/>
                    <button type="submit">View My Database</button>
                  </form>
                  </li>';
            }

            if ((int)$perm_level === 2 || (int)$perm_level === 3) { // TA or Professor
              echo '<li>
                      <form action="grading.php" method="get">
                        <button type="submit">Grading</button>
                      </form>
                    </li>';
            }

              if ((int)$perm_level === 3) {
              echo '<li>
                  <form action="prof.php" method="get">
                    <button type="submit">Professor Control Panel</button>
                  </form>
                </li>';
              }
          }
          $permStmt->close(); // Close the prepared statement
          echo '</ul>
          </nav>';
        }
        ?>

    <main>
      <?php
        // If user is logged in, show "Sign Out", otherwise show "Sign In"
        if (isset($_SESSION['username'])) {
            echo '<div style="margin: 0 auto; text-align: center;">
              <form action="code-logout.php" method="post">
                <p>Logged in as ' . htmlspecialchars($_SESSION['username']) . '</p>
                <button type="submit">Sign Out</button>
              </form>
              <form action="changepw.php" method="get">
                <button type="submit">Change Password</button>
              </form>
                  </div>';
        } else {
          echo '<form action="code-login.php" method="post">
                  <button type="submit">Sign In</button>
                </form>';
        }
      ?>

      <!-- List of databases: -->
      <?php
      // Query to fetch database details along with team members and grades
      $sql = "SELECT 
        db.db_id, 
        db.db_name, 
        db.db_description, 
        db.last_updated, 
        grades.grade,
        team.team_id,
        CONCAT(
        COALESCE(user1.fname, ''), ' ', COALESCE(user1.lname, ''), ', ',
        COALESCE(user2.fname, ''), ' ', COALESCE(user2.lname, ''), ', ',
        COALESCE(user3.fname, ''), ' ', COALESCE(user3.lname, '')
        ) AS team_members
        FROM db
        LEFT JOIN grades ON db.db_id = grades.db_id
        LEFT JOIN team ON db.team_id = team.team_id
        LEFT JOIN `users` AS user1 ON team.user1 = user1.username
        LEFT JOIN `users` AS user2 ON team.user2 = user2.username
        LEFT JOIN `users` AS user3 ON team.user3 = user3.username
        GROUP BY db.db_id, db.db_name, db.db_description, db.last_updated, grades.grade, team.team_id, user1.fname, user1.lname, user2.fname, user2.lname, user3.fname, user3.lname";

      $result = $conn->query($sql); // Execute the query

      // Check if any databases were found
      if ($result->num_rows > 0) {
        // Loop through each database and display its details
        while ($row = $result->fetch_assoc()) {
        echo '<section class="database-card">'; // Start a new section for each database
        echo '<h2>' . htmlspecialchars($row['db_name']) . '</h2>'; // Display database name
        $desc = $row['db_description'];
        if (strlen($desc) > 200) {
          $desc = substr($desc, 0, 200) . '...';
        }
        echo '<p>' . htmlspecialchars($desc) . '</p>';
        echo '<a href="details.php?id=' . htmlspecialchars($row['db_id']) . '">View Details</a>'; // Link to view details
        echo '</section>'; // End the section
        }
      } else {
        // Display an error message if no databases were found
        echo '<p><b>ERROR!</b> No databases found.</p>';
      }
      ?>
    </main>
  </div>
  </body>
</html>
