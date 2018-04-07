<?php

$LeftPersonID = addslashes($_REQUEST['LeftPersonID']);
$RightPersonID = addslashes($_REQUEST['RightPersonID']);
$StartDate = addslashes($_REQUEST['StartDate']);
$Accepted = addslashes($_REQUEST['Accepted']);

include 'lib.php';

   	$sql = "INSERT INTO PersonModule_Friendship (LeftPersonID,RightPersonID,StartDate,Accepted) VALUES";
   	$sql .= "('$LeftPersonID','$RightPersonID', '$StartDate', '$Accepted')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>