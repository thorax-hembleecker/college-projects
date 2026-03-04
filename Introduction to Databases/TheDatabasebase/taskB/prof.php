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

// Verify that the user is a professor (perm_level 3)
$sql = "SELECT perm_level FROM permissions WHERE username = ?";
$permStmt = $conn->prepare($sql);
$permStmt->bind_param("s", $loggedUser);
$permStmt->execute();
$result = $permStmt->get_result();

if ($result->num_rows === 0) {
    // No permissions record found; redirect to login
    header("Location: login.html");
    exit;
}

$row = $result->fetch_assoc();
if ((int)$row['perm_level'] !== 3) {
    // Not a professor; redirect
    header("Location: login.html");
    exit;
}

$permStmt->close();
?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>Professor Control Panel | The Databasebase</title>
        <link rel="stylesheet" href="style.css" />
    </head>
    <body>
        <header>
            <h1><a href="index.php">The Databasebase</a></h1>
        </header>
        <div class="prof">
            <nav>
                <ul>
                    <li>
                        <form action="grading.php" method="get">
                            <button type="submit">Grading</button>
                        </form>
                    </li>
                    <li>
                        <p>Logged in as <?php echo htmlspecialchars($loggedUser); ?></p>
                    </li>
                    <li>
                        <form action="code-logout.php" method="post">
                            <button type="submit">Sign Out</button>
                        </form>
                    </li>
                </ul>
            </nav>
            <h1>Professor Control Panel</h1>
            <h4 style="text-align: center;">NOTE: All updates and deletions are submitted to the database ONLY when you click the "Submit Changes" button at the bottom of each section.</h4>
            <main>

                <!-- Combined Users & Permissions Table Management -->
                <section class="control-panel-section">
                    <h2>Users & Permissions Table Management</h2>
                    <p>Note: You cannot modify your own permission entry, or delete your own profile.</p>
                    <p>Permission Key: 1 = Student, 2 = TA, 3 = Professor</p>
                    <form action="code-update_users_and_perms.php" method="post">
                        <input type="hidden" name="table" value="users">
                        <table id="usersTable" border="1" style="width:100%;">
                            <colgroup>
                                <col style="width: 20%;">
                                <col style="width: 20%;">
                                <col style="width: 20%;">
                                <col style="width: 10%;">
                                <col style="width: 10%;">
                                <col style="width: 5%;">
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Title</th>
                                    <th>Permission Level</th>
                                    <th class="del">Delete Entry (type Y)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php
                                $combinedResult = $conn->query(
                                    "SELECT users.username, users.pw, users.fname, users.lname, users.title, permissions.perm_level 
                                    FROM users 
                                    INNER JOIN permissions ON users.username = permissions.username"
                                );
                                if ($combinedResult) {
                                    while ($user = $combinedResult->fetch_assoc()) {
                                        $uname = htmlspecialchars($user['username']);
                                        echo "<tr>";
                                        echo '<td><input type="text" name="users[existing]['.$uname.'][username]" value="'.$uname.'" /></td>';
                                        echo '<td><input type="text" name="users[existing]['.$uname.'][fname]" value="'.htmlspecialchars($user['fname']).'" /></td>';
                                        echo '<td><input type="text" name="users[existing]['.$uname.'][lname]" value="'.htmlspecialchars($user['lname']).'" /></td>';
                                        echo '<td><input type="text" name="users[existing]['.$uname.'][title]" value="'.htmlspecialchars($user['title']).'" /></td>';
                                        if ($uname === htmlspecialchars($loggedUser)) {
                                            echo '<td><input type="number" name="users[existing]['.$uname.'][perm_level]" value="'.htmlspecialchars($user['perm_level']).'" readonly /></td>';
                                        } else {
                                            echo '<td><input type="number" name="users[existing]['.$uname.'][perm_level]" value="'.htmlspecialchars($user['perm_level']).'" /></td>';
                                        }
                                        echo '<td><input type="text" name="users[existing]['.$uname.'][delete]" /></td>';
                                        echo "</tr>";
                                    }
                                }
                                ?>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_users">Submit Changes</button>
                    </form>
                    <!-- New User & Permission Entry Section -->
                    <form action="code-add_user.php" method="post" style="margin-top:20px;">
                        <input type="hidden" name="table" value="users">
                        <h3>Add New User</h3>
                        <table border="1" style="width:100%;">
                        <colgroup>
                                <col style="width: 15%;">
                                <col style="width: 15%;">
                                <col style="width: 20%;">
                                <col style="width: 20%;">
                                <col style="width: 15%;">
                                <col style="width: 15%;">
                            </colgroup>

                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Password</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Title</th>
                                    <th>Permission Level</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="text" name="users[new][]" placeholder="(required)" required /></td>
                                    <td><input type="text" name="users[new][]" placeholder="(required)" required /></td>
                                    <td><input type="text" name="users[new][]" placeholder="(required)" required/></td>
                                    <td><input type="text" name="users[new][]" placeholder="(required)" required/></td>
                                    <td><input type="text" name="users[new][]" /></td>
                                    <td><input type="number" name="users[new][]" placeholder="(required, 1-3)" required /></td>
                                </tr>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_new_users">Submit New User</button>
                    </form>
                </section>


                <!-- Teams Table Management -->
                <section class="control-panel-section">
                    <h2>Teams Table Management</h2>
                    <form action="code-update_teams.php" method="post">
                        <input type="hidden" name="table" value="team">
                        <table id="teamTable" border="1" style="width:100%;">
                            <colgroup>
                                <col style="width: 5%;">
                                <col style="width: 30%;">
                                <col style="width: 30%;">
                                <col style="width: 30%;">
                                <col style="width: 10%;">
                            </colgroup>

                            <thead>
                                <tr>
                                    <th>Team ID</th>
                                    <th>Member 1</th>
                                    <th>Member 2</th>
                                    <th>Member 3</th>
                                    <th class="del">Delete Entry (type Y)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php
                                $teamResult = $conn->query("SELECT * FROM team");
                                if ($teamResult) {
                                    while ($team = $teamResult->fetch_assoc()) {
                                        echo "<tr>";
                                        echo '<td><input type="number" name="team[existing]['.$team['team_id'].'][team_id]" value="'.htmlspecialchars($team['team_id']).'" readonly /></td>';
                                        echo '<td><input type="text" name="team[existing]['.$team['team_id'].'][user1]" value="'.htmlspecialchars($team['user1']).'" /></td>';
                                        echo '<td><input type="text" name="team[existing]['.$team['team_id'].'][user2]" value="'.htmlspecialchars($team['user2']).'" /></td>';
                                        echo '<td><input type="text" name="team[existing]['.$team['team_id'].'][user3]" value="'.htmlspecialchars($team['user3']).'" /></td>';
                                        echo '<td><input type="text" name="team[existing]['.$team['team_id'].'][delete]" /></td>';
                                        echo "</tr>";
                                    }
                                }
                                ?>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_team">Submit Changes</button>
                    </form>
                    <!-- New Team Entry Section -->
                    <form action="code-add_team.php" method="post" style="margin-top:20px;">
                        <input type="hidden" name="table" value="team">
                        <h3>Add New Team</h3>
                        <table border="1" style="width:100%;">
                            <colgroup>
                                <col style="width: 15%;">
                                <col style="width: 30%;">
                                <col style="width: 30%;">
                                <col style="width: 25%;">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>Team ID</th>
                                    <th>Member 1</th>
                                    <th>Member 2</th>
                                    <th>Member 3</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="number" name="team[id]" placeholder="(required)" required /></td>
                                    <td><input type="text" name="team[new][]" placeholder="(required)" required /></td>
                                    <td><input type="text" name="team[new][]" /></td>
                                    <td><input type="text" name="team[new][]" /></td>
                                </tr>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_new_team">Submit New Team</button>
                    </form>
                </section>

                <!-- Database Table Management -->
                <section class="control-panel-section">
                    <h2>Database Table Management</h2>
                    <form action="code-update_databases.php" method="post">
                        <input type="hidden" name="table" value="`db`">
                        <table id="dbTable" border="1" style="width:100%;">
                            <colgroup>
                                <col style="width: 5%;">
                                <col style="width: 5%;">
                                <col style="width: 20%;">
                                <col style="width: 30%;">
                                <col style="width: 2.5%;">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>Database ID</th>
                                    <th>Team ID</th>
                                    <th>Database Name</th>
                                    <th>Description</th>
                                    <th class="del">Delete Entry (type Y)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php
                                $dbResult = $conn->query("SELECT * FROM db");
                                if ($dbResult) {
                                    while ($db = $dbResult->fetch_assoc()) {
                                        echo "<tr>";
                                        echo '<td><input type="number" name="db[existing]['.$db['db_id'].'][db_id]" value="'.htmlspecialchars($db['db_id']).'" readonly /></td>';
                                        echo '<td><input type="number" name="db[existing]['.$db['db_id'].'][team_id]" value="'.htmlspecialchars($db['team_id']).'" readonly /></td>';
                                        echo '<td><input type="text" name="db[existing]['.$db['db_id'].'][db_name]" value="'.htmlspecialchars($db['db_name']).'" /></td>';
                                        echo '<td><textarea name="db[existing]['.$db['db_id'].'][db_description]" style="width:100%; box-sizing: border-box; resize: vertical;">'.htmlspecialchars($db['db_description']).'</textarea></td>';
                                        echo '<td><input type="text" name="db[existing]['.$db['db_id'].'][delete]" /></td>';
                                        echo "</tr>";
                                    }
                                }
                                ?>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_db">Submit Changes</button>
                    </form>
                    <!-- New Database Entry Section -->
                    <form action="code-add_database.php" method="post" style="margin-top:20px;">
                        <input type="hidden" name="table" value="`db`">
                        <h3>Add New Database Entry</h3>
                        <table border="1" style="width:100%;">
                            <col style="width: 5%;">
                            <col style="width: 40%;">
                            <col style="width: 50%;">
                            <thead>
                                <tr>
                                    <th>Team ID</th>
                                    <th>Database Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="number" name="db[new][]" placeholder="(required)" required /></td>
                                    <td><input type="text" name="db[new][]" placeholder="(required)" required /></td>
                                    <td><textarea name="db[new][]" style="width:100%; box-sizing: border-box; resize: vertical;" required></textarea></td>
                                </tr>
                            </tbody>
                        </table>
                        <br>
                        <button class="submit-changes" type="submit" name="submit_new_db">Submit New Database Entry</button>
                    </form>
                </section>
            </main>
        </div>
    </body>
</html>