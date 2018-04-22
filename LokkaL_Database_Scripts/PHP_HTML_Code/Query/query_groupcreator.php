<?php

  $PersonID = $_REQUEST['PersonID'];
  include 'lib.php';

   $sql = "SELECT gs.GroupID
   , gs.GroupName
   FROM  GroupModule_GroupSession as gs
   WHERE NOW() BETWEEN gs.StartTime AND IFNULL(gs.EndTime,'9999-12-31')
   AND gs.GroupCreatorID = " . $PersonID;

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $GroupID = stripslashes($ary["GroupID"]);
  $GroupName = stripslashes($ary["GroupName"]);
  echo "$GroupID,$GroupName\n";
}
 mysql_close($link_id);

?>