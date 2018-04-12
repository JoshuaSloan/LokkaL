<?php

  $Email = $_REQUEST['Email'];
  $Password = $_REQUEST['Password'];

  include 'lib.php';

   $sql = "SELECT PersonID, FirstName, LastName, DateOfBirth, Email, Password 
   		FROM PersonModule_Person 
   		WHERE Email = '" . $Email . "' AND Password = '" . $Password . "'";

$result = mysql_query($sql);
while($ary = mysql_fetch_array($result)) {
  $PersonID = stripslashes($ary["PersonID"]);
  $FirstName = stripslashes($ary["FirstName"]);
  $LastName = stripslashes($ary["LastName"]);
  $DateOfBirth = stripslashes($ary["DateOfBirth"]);
  $Email = stripslashes($ary["Email"]);
  $Password = stripslashes($ary["Password"]);
  echo "$PersonID,$FirstName,$LastName,$DateOfBirth,$Email,$Password\n";
}
 mysql_close($link_id);

?>
