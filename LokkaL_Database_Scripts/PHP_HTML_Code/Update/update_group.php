<?php

$GroupID = $_REQUEST['GroupMemberID'];

include 'lib.php';


$sql = "UPDATE GroupModule_GroupSession SET EndTime =  NOW() WHERE GroupID = " . $GroupID;

//   echo "$sql\n";

$result = mysql_query($sql);
echo mysql_affected_rows($link_id);
mysql_close($link_id);
 
?>
