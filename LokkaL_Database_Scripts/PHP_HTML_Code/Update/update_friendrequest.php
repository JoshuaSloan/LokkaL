<?php

$FriendshipID = addslashes($_REQUEST['FriendshipID']);
$Accepted = addslashes($_REQUEST['Accepted']);

include 'lib.php';


$sql = "UPDATE PersonModule_Friendship SET Accepted = " . $Accepted . " WHERE FriendshipID = " . $FriendshipID;

//   echo "$sql\n";
   $result = mysql_query($sql);
  echo mysql_affected_rows($link_id);
} else {
  echo 0;
}
 mysql_close($link_id);
 
?>
