CREATE SCHEMA PersonModule;
GO
CREATE TABLE PersonModule.Person
(
	PersonID int IDENTITY (1,1) NOT NULL
	, FirstName varchar(50) NOT NULL
	, LastName varchar(50) NOT NULL
	, DateOfBirth date NOT NULL
	, Email varchar(50) NOT NULL
	, Password varchar(16) NOT NULL
	, Active BIT NULL
	CONSTRAINT PK_Person_PersonID PRIMARY KEY CLUSTERED (PersonID)
);
GO


CREATE TABLE PersonModule.Friendship
(
	FriendshipID INT IDENTITY(1,1) NOT NULL
	, LeftPersonID INT NOT NULL
	, RightPersonID INT NOT NULL
	, StartDate datetime NOT NULL
	, EndDate datetime NULL
	, Active BIT NULL
	CONSTRAINT PK_Friendship_FriendshipID PRIMARY KEY CLUSTERED (FriendshipID)
	, CONSTRAINT FK_Person_LeftPersonID FOREIGN KEY (LeftPersonID)
	REFERENCES PersonModule.Person (PersonID)
	, CONSTRAINT FK_Person_RightPersonID FOREIGN KEY (RightPersonID)
	REFERENCES PersonModule.Person (PersonID)
)
--SELECT * FROM PersonModule.Person
--SELECT * FROM PersonModule.Friendship