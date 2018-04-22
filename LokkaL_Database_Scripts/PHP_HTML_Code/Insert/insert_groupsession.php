<?php

$GroupName = addslashes($_REQUEST['GroupName']);
$GroupCreatorID = addslashes($_REQUEST['GroupCreatorID']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupSession (GroupName,GroupCreatorID,StartTime) 
   	VALUES ('" . $GroupName . "', " . $GroupCreatorID . ", NOW())";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>