<?php 
/**
* Note: You must download the database from http://isslab-udayton.github.io/SIAD/uscities.sql  
* and import to your VM mysql:
* $ mysql –u root –pseedubuntu
* mysql> use SIAD_lab7; 
* mysql> source uscities.sql; 
* mysql> show tables; # just for testing
* mysql> select * from zips; # just for testing
*
*
*/
	// get the q parameter from URL
	$query = $_REQUEST["q"];
	//echo $query . $query . "<br>" ;
	if(!isset($query)) exit; 
	$mysqli = new mysqli('localhost', 'SIAD_lab7', 'secretpass', 'SIAD_lab7');
	if($mysqli->connect_error){
		die('Connection to the database has error: ' . $mysqli->connect_error); 
	}
	$sql = "SELECT city, state, zip  FROM zips WHERE city LIKE '%{$query}%';";
	//echo $sql . "<br>";
	$result = $mysqli->query($sql);	
	if($result->num_rows >0 ){
		while($row = $result->fetch_assoc()){
			echo htmlentities($row['city']) . 
				 ", " . htmlentities($row['state']) . 
				 ", " . htmlentities($row['zip']) . "<br>";
		}
	}else{
		echo "No matching";
	}

?>

