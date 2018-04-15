CREATE TABLE PersonModule_Person
(
	PersonID int NOT NULL AUTO_INCREMENT
	, FirstName varchar(50) NOT NULL
	, LastName varchar(50) NOT NULL
	, DateOfBirth date NOT NULL
	, Email varchar(50) NOT NULL
	, Password varchar(16) NOT NULL
	, Active BIT NULL
	, CONSTRAINT PK_Person_PersonID PRIMARY KEY CLUSTERED (PersonID)
);

CREATE TABLE PersonModule_Friendship
(
	FriendshipID INT NOT NULL AUTO_INCREMENT
	, LeftPersonID INT NOT NULL
	, RightPersonID INT NOT NULL
	, StartDate datetime NOT NULL
	, EndDate datetime NULL
	, ResponseTypeID INT NOT NULL
	, Active BIT NULL
	, CONSTRAINT PK_Friendship_FriendshipID PRIMARY KEY CLUSTERED (FriendshipID)
	, CONSTRAINT FK_Person_LeftPersonID FOREIGN KEY (LeftPersonID)
	REFERENCES PersonModule_Person (PersonID)
	, CONSTRAINT FK_Person_RightPersonID FOREIGN KEY (RightPersonID)
	REFERENCES PersonModule_Person (PersonID)
	, CONSTRAINT FK_FRequest_ResponseTypeID FOREIGN KEY (ResponseTypeID)
	REFERENCES Request_ResponseType (ResponseTypeID)
);
--SELECT * FROM PersonModule_Person;
--SELECT * FROM PersonModule_Friendship;
--DROP TABLE PersonModule_Friendship;
--DROP TABLE PersonModule_Person;