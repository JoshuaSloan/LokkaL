<?php

  $GroupID = $_REQUEST['GroupID'];

  include 'lib.php';

   $sql = 'SELECT gm.GroupName
	, p.FirstName + ' ' + p.LastName as PersonName
	, gml.Latitude 
	, gml.Longitude
FROM GroupModule_GroupSession as gs
INNER JOIN GroupModule_GroupMembers as gm
ON gs.GroupID = gm.GroupID
INNER JOIN GroupModule_GroupMemberLocation as gml
ON gml.GroupMemberID = gm.GroupMemberID
INNER JOIN PersonModule_Person as p
ON p.PersonID = gml.PersonID
WHERE ISNULL (gs.Active,1) <> 0
AND ISNULL (gm.Active,1) <> 0
AND ISNULL (gml.Active,1) <> 0
AND ISNULL (p.Active,1) <> 0
AND gm.Accepted = 1
AND CAST(NOW() as DATE) BETWEEN CAST(gm.StartDate as DATE) AND CAST(ISNULL(gm.EndDate, /'9999-12-31/') as DATE)
AND GroupID = '.$GroupID;

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $GroupName = stripslashes($ary["GroupName"]);
  $PersonName = stripslashes($ary["PersonName"]);
  $Latitude = stripslashes($ary["Latitude"]);
  $Longitude = stripslashes($ary["Longitude"]);
  echo "$GroupName,$PersonName,$Latitude,$Longitude\n";
}
 mysql_close($link_id);

?>