<?php

  $PersonID = $_REQUEST['PersonID'];

  include 'lib.php';

   $sql = "SELECT f.FriendshipID
  ,lp.PersonID
  ,lp.FirstName + ' ' + lp.LastName as LeftPersonName
FROM PersonModule_Friendship as f
INNER JOIN PersonModule_Person as rp
ON p.PersonID = f.RightPersonID
INNER JOIN PersonModule_Person as lp
ON lp.PersonID AND f.LeftPersonID
WHERE ISNULL(f.Active,1) <> 0
AND ISNULL(rp.Active,1) <> 0
AND ISNULL(lp.Active,1) <> 0
AND f.Accepted IS NULL
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



