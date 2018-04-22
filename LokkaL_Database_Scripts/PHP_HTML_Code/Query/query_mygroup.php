<?php

  $PersonID = $_REQUEST['PersonID'];
  include 'lib.php';

   $sql = "SELECT gs.GroupID
   , gs.GroupName
   FROM  GroupModule_GroupMembers as gm
   INNER JOIN GroupModule_GroupSession as gs
   ON gs.GroupID = gm.GroupID
   WHERE NOW() BETWEEN gm.StartTime AND IFNULL(gm.EndTime,'9999-12-31')
   AND gm.PersonID = " . $PersonID;

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $GroupID = stripslashes($ary["GroupID"]);
  $GroupName = stripslashes($ary["GroupName"]);
  echo "$GroupID,$GroupName\n";
}
 mysql_close($link_id);

?>