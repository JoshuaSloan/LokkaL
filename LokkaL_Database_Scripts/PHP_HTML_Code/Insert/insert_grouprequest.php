<?php

$GroupName = addslashes($_REQUEST['GroupName']);
$GroupCreatorID = addslashes($_REQUEST['GroupCreatorID']);
$StartTime = addslashes($_REQUEST['StartTime']);
$Accepted = addslashes($_REQUEST['Accepted']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupSession (GroupName,GroupCreatorID,StartTime,Accepted) VALUES";
   	$sql .= "('$GroupName','$GroupCreatorID', '$StartTime', '$Accepted')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>