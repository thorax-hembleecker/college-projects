<?php
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
    <meta charset="utf-8">
    <title>Database Grading | The Databasebase</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <header>
        <h1><a href="index.php">The Databasebase</a></h1>
    </header>
    <main>
        <?php
        session_start();
        $loggedUser = $_SESSION['username'] ?? null;

        if ($loggedUser) {
            // Fetch the user's permission level.
            $stmt = $conn->prepare("SELECT perm_level FROM permissions WHERE username = ?");
            $stmt->bind_param("s", $loggedUser);
            $stmt->execute();
            $result = $stmt->get_result();
            $row = $result->fetch_assoc();
            $perm_level = $row['perm_level'] ?? null;

            // Check if the user has TA (2) or Professor (3) permission.
            if ($perm_level == 2 || $perm_level == 3) {
                // Query the list of databases with any existing grade.
                $sql = "SELECT d.db_id, d.db_name, d.db_description, d.team_id, t.user1, t.user2, t.user3, g.grade 
                    FROM db d 
                    LEFT JOIN grades g ON d.db_id = g.db_id 
                    JOIN team t ON d.team_id = t.team_id 
                    ORDER BY d.db_id";
                $db_result = $conn->query($sql);
                ?>
                <form action="code-submit_grades.php" method="POST" class="dbedit">
                    <h1>Grading</h1>
                    <p>Select a grade for each database project (leave blank if not graded yet).</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Database Name</th>
                                <th>Team Information</th>
                                <th>Grade</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php while ($db_row = $db_result->fetch_assoc()) { ?>
                                <tr>
                                    <td>
                                        <a href="details.php?id=<?php echo htmlspecialchars($db_row['db_id']); ?>" class="details-link" title="View Details">
                                            <?php echo htmlspecialchars($db_row['db_name']); ?>
                                        </a>
                                    </td>
                                    <td>
                                        <strong>Team <?php echo htmlspecialchars($db_row['team_id']); ?></strong><br>
                                        <?php 
                                        $members = [$db_row['user1'], $db_row['user2'], $db_row['user3']];
                                        // Filter out empty members.
                                        $members = array_filter($members);
                                        echo htmlspecialchars(implode(', ', $members));
                                        ?>
                                    </td>
                                    <td class="grade">
                                        <input type="number" name="grades[<?php echo htmlspecialchars($db_row['db_id']); ?>]" 
                                               placeholder="(0-100)" min="0" max="100" 
                                               value="<?php echo isset($db_row['grade']) ? htmlspecialchars($db_row['grade']) : ''; ?>">
                                    </td>
                                </tr>
                            <?php } ?>
                        </tbody>
                    </table>
                    <button type="submit">Submit Grades</button>
                </form>
                <?php
            } else {
                header("Location: index.php");
                exit;
            }
        } else {
            header("Location: login.html");
            exit;
        }
        ?>
    </main>
</body>
</html>