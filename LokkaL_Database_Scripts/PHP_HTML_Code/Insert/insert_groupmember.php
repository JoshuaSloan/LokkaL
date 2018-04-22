<?php
$GroupID = addslashes($_REQUEST['GroupID']);
$PersonID = addslashes($_REQUEST['PersonID']);
$ResponseTypeID = addslashes($_REQUEST['ResponseTypeID']);

include 'lib.php';

   	$sql = "INSERT INTO GroupModule_GroupMembers (GroupID, PersonID, StartTime, ResponseTypeID)
   	VALUES (" . $GroupID . ", " . $PersonID . ", NOW(), " 
   	. $ResponseTypeID . ")";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>