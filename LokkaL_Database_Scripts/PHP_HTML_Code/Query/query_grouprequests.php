<?php

  $PersonID = $_REQUEST['PersonID'];

  include 'lib.php';

   $sql = "SELECT gm.GroupMemberID
  , gs.GroupName
  , CONCAT(p.FirstName, ' ', p.LastName) as PersonName
FROM GroupModule_GroupSession as gs
INNER JOIN GroupModule_GroupMembers as gm
ON gm.GroupID = gs.GroupID
INNER JOIN PersonModule_Person as p
ON p.PersonID = gs.GroupCreatorID
INNER JOIN Request_ResponseType as rt
ON rt.ResponseTypeID = gm.ResponseTypeID
WHERE IFNULL(gm.Active,1) <> 0
AND IFNULL(gs.Active,1) <> 0
AND rt.Name = 'Pending'
AND gm.PersonID = " . $PersonID;
	
$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $GroupMemberID = stripslashes($ary["GroupMemberID"]);
  $GroupName = stripslashes($ary["GroupName"]);
  $PersonName = stripslashes($ary["PersonName"]);
  echo "$GroupMemberID,$GroupName,$PersonName\n";
}
 mysql_close($link_id);

?>



