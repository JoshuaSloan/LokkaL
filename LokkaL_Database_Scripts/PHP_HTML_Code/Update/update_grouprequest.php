<?php

$GroupMemberID = $_REQUEST['GroupMemberID'];
$ResponseTypeID = $_REQUEST['ResponseTypeID'];

include 'lib.php';


$sql = "UPDATE GroupModule_GroupMembers SET ResponseTypeID = " . $ResponseTypeID . " WHERE GroupMemberID = " . $GroupMemberID;

//   echo "$sql\n";

$result = mysql_query($sql);
echo mysql_affected_rows($link_id);
mysql_close($link_id);
 
?>
