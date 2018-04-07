<?php

$FirstName = addslashes($_REQUEST['FirstName']);
$LastName = addslashes($_REQUEST['LastName']);
$DateOfBirth = addslashes($_REQUEST['DateOfBirth']);
$Email = addslashes($_REQUEST['Email']);
$Password = addslashes($_REQUEST['Password']);

include 'lib.php';

   	$sql = "INSERT INTO PersonModule_Person (FirstName, LastName, DateOfBirth, Email, Password) VALUES";
   	$sql .= "('$FirstName','$LastName', '$DateOfBirth', '$Email', '$Password')";
   	//echo "$sql\n";
   	$result = mysql_query($sql);
  	echo mysql_affected_rows($link_id);
 	mysql_close($link_id);
 
?>
