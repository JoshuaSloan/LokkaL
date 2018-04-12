<?php

  $PersonID = $_REQUEST['PersonID'];

  include 'lib.php';

   $sql = "SELECT f.FriendshipID
  ,CASE WHEN f.LeftPersonID = " . $PersonID 
  . "THEN  rp.FirstName + ' ' + rp.LastName
  ELSE lp.FirstName + ' ' + lp.LastName END as PersonName
FROM PersonModule_Friendship as f
INNER JOIN PersonModule_Person as rp
ON p.PersonID = f.RightPersonID
INNER JOIN PersonModule_Person as lp
ON lp.PersonID AND f.LeftPersonID
WHERE ISNULL(f.Active,1) <> 0
AND ISNULL(rp.Active,1) <> 0
AND ISNULL(lp.Active,1) <> 0
AND f.Accepted = 1
AND (f.RightPersonID = " . $PersonID . " OR f.LeftPersonID = ". $PersonID . ")"
	
$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $FriendshipID = stripslashes($ary["FriendshipID"]);
  $PersonName = stripslashes($ary["PersonName"]);
  echo "$FriendshipID,$PersonName\n";
}
 mysql_close($link_id);

?>



