<?php

  $PersonID = $_REQUEST['PersonID'];

  include 'lib.php';

   $sql = "SELECT f.FriendshipID
, lp.PersonID
, CONCAT(lp.FirstName,' ' ,lp.LastName) as LeftPersonName
FROM PersonModule_Friendship as f 
INNER JOIN PersonModule_Person as rp 
ON rp.PersonID = f.RightPersonID 
INNER JOIN PersonModule_Person as lp 
ON lp.PersonID = f.LeftPersonID 
INNER JOIN Request_ResponseType as rt
ON rt.ResponseTypeID = f.ResponseTypeID
WHERE IFNULL(f.Active,1) <> 0 
AND IFNULL(rp.Active,1) <> 0
AND IFNULL(lp.Active,1) <> 0 
AND rt.Name = 'Pending'
AND f.RightPersonID = " . $PersonID;

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $FriendshipID = stripslashes($ary["FriendshipID"]);
  $PersonID = stripslashes($ary["PersonID"]);
  $LeftPersonName = stripslashes($ary["LeftPersonName"]);
  echo "$FriendshipID,$PersonID,$LeftPersonName\n";
}
 mysql_close($link_id);

?>


