<?php

$GroupMemberID = addslashes($_REQUEST['GroupMemberID']);
$LocationTime = addslashes($_REQUEST['LocationTime']);
$Latitude = addslashes($_REQUEST['Latitude']);
$Longitude = addslashes($_REQUEST['Longitude']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupMemberLocation (GroupMemberID,LocationTime,Latitude,Longitude) VALUES";
   	$sql .= "('$GroupMemberID', '$LocationTime', '$Latitude', '$Longitude')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>