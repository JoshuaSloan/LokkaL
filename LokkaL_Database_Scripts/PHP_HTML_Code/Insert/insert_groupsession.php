<?php

$GroupName = addslashes($_REQUEST['GroupName']);
$GroupCreatorID = addslashes($_REQUEST['GroupCreatorID']);
$StartTime = addslashes($_REQUEST['StartTime']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupSession (GroupName,GroupCreatorID,StartTime) VALUES";
   	$sql .= "('$GroupName','$GroupCreatorID', '$StartTime')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>