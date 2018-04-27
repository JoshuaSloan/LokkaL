<?php

$GroupMemberID = addslashes($_REQUEST['GroupMemberID']);
$Latitude = addslashes($_REQUEST['Latitude']);
$Longitude = addslashes($_REQUEST['Longitude']);
$BatteryLife = addslashes($_REQUEST['BatteryLife']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupMemberLocation (GroupMemberID,LocationTime,Latitude,Longitude, BatteryLife) 
   	VALUES (" . $GroupMemberID . ", Now(), " . $Latitude . ", "
   	. $Longitude . ", " . $BatteryLife . ")";

   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>