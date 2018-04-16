<?php

$FriendshipID = $_REQUEST['FriendshipID'];
$ResponseTypeID = $_REQUEST['ResponseTypeID'];

include 'lib.php';


$sql = "UPDATE PersonModule_Friendship SET ResponseTypeID = " . $ResponseTypeID . " WHERE FriendshipID = " . $FriendshipID;

//   echo "$sql\n";

$result = mysql_query($sql);
echo mysql_affected_rows($link_id);
mysql_close($link_id);
 
?>
