<?php

$PersonID = addslashes($_REQUEST['PersonID']);
$StartTime = addslashes($_REQUEST['StartTime']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupMembers (PersonID,StartTime) VALUES";
   	$sql .= "('$PersonID', '$StartTime')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>