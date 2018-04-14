<?php

$LeftPersonID = addslashes($_REQUEST['LeftPersonID']);
$Email = addslashes($_REQUEST['Email']);

include 'lib.php';

   	$sql = "INSERT INTO PersonModule_Friendship (LeftPersonID, RightPersonID, StartDate)
   	SELECT " . $LeftPersonID . ", p.PersonID, NOW()
   	FROM PersonModule_Person as p
   	WHERE p.Email = '" . $Email . "'"

   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>