<?php

$GroupMemberID = $_REQUEST['GroupMemberID'];

include 'lib.php';


$sql = "UPDATE GroupModule_GroupMembers SET EndTime = NOW() WHERE GroupMemberID = " . $GroupMemberID;

//   echo "$sql\n";

$result = mysql_query($sql);
echo mysql_affected_rows($link_id);
mysql_close($link_id);
 
?>
