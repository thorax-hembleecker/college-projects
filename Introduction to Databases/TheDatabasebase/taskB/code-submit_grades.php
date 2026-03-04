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
  exit("Connection failed: " . $conn->connect_error);
}

// Check if the current user has TA (2) or Professor (3) permission.
$permStmt = $conn->prepare("SELECT perm_level FROM permissions WHERE username = ?");
$permStmt->bind_param("s", $loggedUser);
$permStmt->execute();
$result = $permStmt->get_result();
$row = $result->fetch_assoc();
$permStmt->close();

if (!$row || ($row['perm_level'] != 2 && $row['perm_level'] != 3)) {
    header("Location: index.php");
    exit;
}

// Process the POST request.
if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST['grades']) && is_array($_POST['grades'])) {
    foreach ($_POST['grades'] as $db_id => $gradeInput) {
        $db_id = (int)$db_id;
        $gradeInput = trim($gradeInput);

        // Determine new grade: if blank, set to NULL.
        if ($gradeInput === "") {
            $newGrade = null;
        } else {
            if (!is_numeric($gradeInput)) {
                continue;
            }
            
            $gradeVal = (int)$gradeInput;
            if ($gradeVal < 0 || $gradeVal > 100) {
                continue;
            }
            $newGrade = $gradeVal;
        }
        
        // Retrieve current grade to check for changes.
        $checkStmt = $conn->prepare("SELECT grade FROM grades WHERE db_id = ?");
        $checkStmt->bind_param("i", $db_id);
        $checkStmt->execute();
        $checkResult = $checkStmt->get_result();
        $rowOld = $checkResult->fetch_assoc();
        // If no row exists, treat current grade as NULL.
        $currentGrade = isset($rowOld['grade']) ? $rowOld['grade'] : null;
        $checkStmt->close();
        
        // Update or insert the grade.
        if (is_null($newGrade)) {
            $gradeStmt = $conn->prepare("INSERT INTO grades (db_id, grade) VALUES (?, NULL) ON DUPLICATE KEY UPDATE grade = NULL");
            $gradeStmt->bind_param("i", $db_id);
        } else {
            $gradeStmt = $conn->prepare("INSERT INTO grades (db_id, grade) VALUES (?, ?) ON DUPLICATE KEY UPDATE grade = ?");
            $gradeStmt->bind_param("iii", $db_id, $newGrade, $newGrade);
        }
    
        $gradeStmt->execute();
        $gradeStmt->close();
        
        // Only add changelog entry if the grade actually changed.
        // Note: Use loose comparison so that null and 0/numeric are accurately compared.
        if ($currentGrade != $newGrade) {
            if (is_null($newGrade)) {
                $change_description = "Grade removed";
            } else {
                $change_description = "Grade updated to " . $newGrade;
            }
            
            $changelogStmt = $conn->prepare("INSERT INTO changelog (username, db_id, change_description, time_of_change) VALUES (?, ?, ?, NOW())");
            $changelogStmt->bind_param("sis", $loggedUser, $db_id, $change_description);
            $changelogStmt->execute();
            $changelogStmt->close();
        }

        // Update the last_updated attribute in the db table.
        $updateStmt = $conn->prepare("UPDATE db SET last_updated = NOW() WHERE db_id = ?");
        $updateStmt->bind_param("i", $db_id);
        $updateStmt->execute();
        $updateStmt->close();
    }

    header("Location: index.php");
    exit;
}

$conn->close();
?>