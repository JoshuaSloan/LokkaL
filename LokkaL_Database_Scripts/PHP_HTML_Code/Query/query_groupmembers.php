<?php

  $GroupID = $_REQUEST['GroupID'];

  include 'lib.php';

   $sql = "SELECT gm.GroupMemberID
 , CONCAT(p.FirstName, ' ', p.LastName) as GroupMemberName
, IFNULL((SELECT gml.Latitude
  FROM GroupModule_GroupMemberLocation as gml
  WHERE gml.GroupMemberID = gm.GroupMemberID
  ORDER BY gml.LocationTime DESC
  LIMIT 1
  ), 41.3314) as Latitude
  ,IFNULL((SELECT gml.Longitude
  FROM GroupModule_GroupMemberLocation as gml
  WHERE gml.GroupMemberID = gm.GroupMemberID
  ORDER BY gml.LocationTime DESC
  LIMIT 1
  ), -105.5911) as Longitude
FROM GroupModule_GroupMembers as gm
INNER JOIN PersonModule_Person as p
ON p.PersonID = gm.PersonID
WHERE gm.ResponseTypeID = 2
AND gm.GroupID = " . $GroupID;

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $GroupMemberID = stripslashes($ary["GroupMemberID"]);
  $GroupMemberName = stripslashes($ary["GroupMemberName"]);
  $Latitude = stripslashes($ary["Latitude"]);
  $Longitude = stripslashes($ary["Longitude"]);
  echo "$GroupMemberID,$GroupMemberName,$Latitude,$Longitude\n";
}
 mysql_close($link_id);

?>