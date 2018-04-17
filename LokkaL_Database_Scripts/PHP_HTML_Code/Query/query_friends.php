<?php

  $PersonID = $_REQUEST['PersonID'];

  include 'lib.php';

   $sql = "SELECT f.FriendshipID
  , CASE WHEN f.LeftPersonID = " . $PersonID 
  . " THEN f.RightPersonID ELSE f.LeftPersonID END as PersonID 
  ,CASE WHEN f.LeftPersonID = " . $PersonID 
  . " THEN  CONCAT(rp.FirstName,' ' ,rp.LastName) ELSE CONCAT(lp.FirstName,' ' ,lp.LastName) END as PersonName
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
AND rt.Name = 'Accepted'
AND (f.RightPersonID = " . $PersonID . " OR f.LeftPersonID = " . $PersonID . ")";
	
$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $FriendshipID = stripslashes($ary["FriendshipID"]);
  $PersonID = stripslashes($ary["PersonID"]);
  $PersonName = stripslashes($ary["PersonName"]);
  echo "$FriendshipID,$PersonID,$PersonName\n";
}
 mysql_close($link_id);

?>



