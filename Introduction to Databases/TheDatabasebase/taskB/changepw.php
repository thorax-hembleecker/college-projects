<?php
// Start session and require login.
session_start();
if (empty($_SESSION['username'])) {
  header('Location: login.html');
  exit;
}

$loggedUser = $_SESSION['username'];
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Change Password | The Databasebase</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="dbhome">
        <header>
            <h1><a href="index.php">The Databasebase</a></h1>
        </header>
        <main>
            <div style="text-align: center;">
                <h2>Change Password</h2>
            </div>
            <form method="post" action="code-changepw.php">
                <p>Current Username: <?php echo htmlspecialchars($loggedUser); ?></p>
                <label>Current Password
                    <input type="password" name="current_password" required>
                </label>
                <br>
                <label>New Password
                    <input type="password" name="new_password" required>
                </label>
                <br>
                <button type="submit">Change Password</button>
            </form>
        </main>
    </div>
</body>
</html>