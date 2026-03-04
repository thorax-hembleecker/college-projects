<?php
// Start session and require login.
session_start();
if (empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

$loggedUser = $_SESSION['username'];

// Database connection details
$host = "localhost";
$db_username = "example";
$db_password = "password";
$db_name = "example_dbb";

$conn = new mysqli($host, $db_username, $db_password, $db_name);
if ($conn->connect_error) {
    exit("Connection failed: " . $conn->connect_error);
}

// Verify current user is a professor (perm_level = 3)
$stmt = $conn->prepare("SELECT perm_level FROM permissions WHERE username = ?");
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

// Ensure the update form was submitted and data exists
if (!isset($_POST['submit_users']) || !isset($_POST['users']['existing'])) {
    header("Location: prof.php");
    exit;
}

$users_post = $_POST['users']['existing'];

// Array to track deleted users for later team cleanup.
$deleted_users = [];

$conn->begin_transaction();
try {
    foreach ($users_post as $username => $data) {
        // If delete marker "Y" is provided (case-insensitive), proceed to delete
        if (isset($data['delete']) && strtoupper(trim($data['delete'])) === "Y") {
            // Do not allow deleting your own record
            if ($username === $loggedUser) {
                continue;
            }
            
            // Before deletion, determine the team membership and update by shifting slots to the left.
            $teamStmt = $conn->prepare("SELECT team_id, user1, user2, user3 FROM team WHERE user1=? OR user2=? OR user3=?");
            $teamStmt->bind_param("sss", $username, $username, $username);
            $teamStmt->execute();
            $teamResult = $teamStmt->get_result();
            while ($teamRow = $teamResult->fetch_assoc()) {
                $team_id = $teamRow['team_id'];
                $u1 = $teamRow['user1'];
                $u2 = $teamRow['user2'];
                $u3 = $teamRow['user3'];
                
                if ($u1 === $username) {
                    // Shift: new user1 becomes user2, user2 becomes user3, user3 cleared.
                    $new_user1 = $u2;
                    $new_user2 = $u3;
                    $new_user3 = null;
                } elseif ($u2 === $username) {
                    // Shift: new user2 becomes user3, user3 cleared.
                    $new_user1 = $u1;
                    $new_user2 = $u3;
                    $new_user3 = null;
                } else { // if ($u3 === $username)
                    $new_user1 = $u1;
                    $new_user2 = $u2;
                    $new_user3 = null;
                }
                
                $updateTeamStmt = $conn->prepare("UPDATE team SET user1 = ?, user2 = ?, user3 = ? WHERE team_id = ?");
                $updateTeamStmt->bind_param("sssi", $new_user1, $new_user2, $new_user3, $team_id);
                $updateTeamStmt->execute();
                $updateTeamStmt->close();
            }
            $teamStmt->close();
            
            // Record that this user will be deleted.
            $deleted_users[] = $username;
            // Delete from permissions first (due to foreign key constraints)
            $delStmt = $conn->prepare("DELETE FROM permissions WHERE username = ?");
            $delStmt->bind_param("s", $username);
            $delStmt->execute();
            $delStmt->close();
            // Then delete from users table
            $delStmt = $conn->prepare("DELETE FROM users WHERE username = ?");
            $delStmt->bind_param("s", $username);
            $delStmt->execute();
            $delStmt->close();
            continue;
        }

        // Retrieve current data to compare with submitted updates.
        $selectStmt = $conn->prepare("SELECT u.pw, u.fname, u.lname, u.title, p.perm_level FROM users u INNER JOIN permissions p ON u.username = p.username WHERE u.username = ?");
        $selectStmt->bind_param("s", $username);
        $selectStmt->execute();
        $result = $selectStmt->get_result();
        if ($result->num_rows === 0) {
            // No record found; skip update for this username.
            $selectStmt->close();
            continue;
        }
        $current = $result->fetch_assoc();
        $selectStmt->close();

        // Detect any attempt to change the password
        if (isset($data['pw']) && $data['pw'] !== $current['pw']) {
            header("Location: index.php");
            exit;
        }

        $updateUser = false;
        $updatePermission = false;

        // Compare user fields: fname, lname, title (password is no longer updated)
        if ($current['fname'] !== $data['fname'] ||
            $current['lname'] !== $data['lname'] ||
            $current['title'] !== $data['title']) {
            $updateUser = true;
        }

        // Compare permission level; do not update if this is the current logged-in professor.
        if ($username !== $loggedUser && $current['perm_level'] != $data['perm_level']) {
            $updatePermission = true;
        }

        if ($updateUser) {
            $updateStmt = $conn->prepare("UPDATE users SET fname = ?, lname = ?, title = ? WHERE username = ?");
            $updateStmt->bind_param("ssss", $data['fname'], $data['lname'], $data['title'], $username);
            $updateStmt->execute();
            $updateStmt->close();
        }

        if ($updatePermission) {
            $updatePermStmt = $conn->prepare("UPDATE permissions SET perm_level = ? WHERE username = ?");
            $updatePermStmt->bind_param("is", $data['perm_level'], $username);
            $updatePermStmt->execute();
            $updatePermStmt->close();
        }

        // Update username if a new one is provided and it differs from the current username.
        if (isset($data['username']) && $data['username'] !== $username) {
            $new_username = $data['username'];

            // Update team table columns that reference the username
            foreach (['user1', 'user2', 'user3'] as $col) {
                $updateStmt = $conn->prepare("UPDATE team SET $col = ? WHERE $col = ?");
                $updateStmt->bind_param("ss", $new_username, $username);
                $updateStmt->execute();
                $updateStmt->close();
            }

            // Update changelog table
            $updateStmt = $conn->prepare("UPDATE changelog SET username = ? WHERE username = ?");
            $updateStmt->bind_param("ss", $new_username, $username);
            $updateStmt->execute();
            $updateStmt->close();

            // Update permissions table
            $updateStmt = $conn->prepare("UPDATE permissions SET username = ? WHERE username = ?");
            $updateStmt->bind_param("ss", $new_username, $username);
            $updateStmt->execute();
            $updateStmt->close();

            // Finally, update the users table primary key
            $updateStmt = $conn->prepare("UPDATE users SET username = ? WHERE username = ?");
            $updateStmt->bind_param("ss", $new_username, $username);
            $updateStmt->execute();
            $updateStmt->close();

            // If the current user changed their own username, update the session accordingly.
            if ($username === $loggedUser) {
                $_SESSION['username'] = $new_username;
                $loggedUser = $new_username;
            }
        }
    }

    // Clean up the team table for any deleted users.
    if (!empty($deleted_users)) {
        $teamQuery = "SELECT team_id, user1, user2, user3 FROM team";
        $teamResult = $conn->query($teamQuery);
        while ($team = $teamResult->fetch_assoc()) {
            $members = [];
            // collect members that have not been deleted
            if (!in_array($team['user1'], $deleted_users)) {
                $members[] = $team['user1'];
            }
            if (!empty($team['user2']) && !in_array($team['user2'], $deleted_users)) {
                $members[] = $team['user2'];
            }
            if (!empty($team['user3']) && !in_array($team['user3'], $deleted_users)) {
                $members[] = $team['user3'];
            }

            if (empty($members)) {
                // Team has no remaining members; delete the team row.
                $delTeamStmt = $conn->prepare("DELETE FROM team WHERE team_id = ?");
                $delTeamStmt->bind_param("i", $team['team_id']);
                $delTeamStmt->execute();
                $delTeamStmt->close();
            } else {
                // Update team row: assign remaining users in order.
                $user1 = $members[0];
                $user2 = isset($members[1]) ? $members[1] : null;
                $user3 = isset($members[2]) ? $members[2] : null;
                $updTeamStmt = $conn->prepare("UPDATE team SET user1 = ?, user2 = ?, user3 = ? WHERE team_id = ?");
                $updTeamStmt->bind_param("sssi", $user1, $user2, $user3, $team['team_id']);
                $updTeamStmt->execute();
                $updTeamStmt->close();
            }
        }
    }

    // Delete any teams with no members.
    $emptyTeamStmt = $conn->prepare("DELETE FROM team WHERE COALESCE(user1, '') = '' AND COALESCE(user2, '') = '' AND COALESCE(user3, '') = ''");
    $emptyTeamStmt->execute();
    $emptyTeamStmt->close();

    $conn->commit();
    header("Location: prof.php");
    exit;
} catch (Exception $e) {
    $conn->rollback();
    exit("Error updating records: " . $e->getMessage());
}
?>