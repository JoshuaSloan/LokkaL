CREATE SCHEMA GroupModule;
GO
CREATE TABLE GroupModule.GroupSession
(
	GroupID int IDENTITY (1,1) NOT NULL
	, GroupName varchar(50) NOT NULL
	, GroupCreatorID INT NOT NULL
	, StartTime datetime NOT NULL
	, EndTime datetime NULL
	, Active BIT NULL
	CONSTRAINT PK_Group_GroupID PRIMARY KEY CLUSTERED (GroupID)
	, CONSTRAINT FK_Person_PersonID FOREIGN KEY (GroupCreatorID)
	REFERENCES PersonModule.Person (PersonID)
);
GO
CREATE TABLE GroupModule.GroupMembers
(
	GroupMemberID INT IDENTITY(1,1) NOT NULL
	, GroupID int NOT NULL
	, PersonID INT NOT NULL
	, StartTime datetime NOT NULL
	, EndTime datetime NULL
	, ColorID INT NULL
	, Active BIT NULL
	CONSTRAINT PK_GroupMember_GroupMemberID PRIMARY KEY CLUSTERED (GroupMemberID)
  , CONSTRAINT FK_GroupMember_PersonID FOREIGN KEY (PersonID)
	REFERENCES PersonModule.Person (PersonID)
	, CONSTRAINT FK_Group_GroupID FOREIGN KEY (GroupID)
	REFERENCES GroupModule.GroupSession (GroupID)
	, CONSTRAINT FK_Color_ColorID FOREIGN KEY (ColorID)
	REFERENCES ColorModule.Color (ColorID)
);
GO
CREATE TABLE GroupModule.GroupMemberLocation
(
	GroupMemberLocationID INT IDENTITY(1,1) NOT NULL
	,GroupMemberID INT NOT NULL
	,LocationTime datetime NOT NULL
	,Latitude DECIMAL(9,6) NOT NULL 
	,Longitude DECIMAL(9,6) NOT NULL 
	, Active BIT NULL
	CONSTRAINT PK_GroupMemberLocation_GroupMemberLocationID PRIMARY KEY CLUSTERED (GroupMemberLocationID)
  , CONSTRAINT FK_GroupMember_MemberID FOREIGN KEY (GroupMemberID)
	REFERENCES GroupModule.GroupMembers (GroupMemberID)
);
GO

CREATE TABLE GroupModule.GroupNotification
(
	GroupNotificationID INT IDENTITY(1,1) NOT NULL
	, NotificationTypeID INT NOT NULL
	, GroupMemberSenderID INT NOT NULL
	, GroupMemberReceiverID INT NOT NULL
	, TimeSent datetime NOT NULL
	, Active bit NULL
	CONSTRAINT PK_GroupMemberNotification_GroupNotificationID PRIMARY KEY CLUSTERED (GroupNotificationID)
  , CONSTRAINT FK_NotificationType_NotificationTypeID FOREIGN KEY (NotificationTypeID)
	REFERENCES NotificationModule.Notification (NotificationTypeID)
  , CONSTRAINT FK_GroupMember_SenderMemberID FOREIGN KEY (GroupMemberSenderID)
	REFERENCES GroupModule.GroupMembers (GroupMemberID)
  , CONSTRAINT FK_GroupMember_ReceiverMemberID FOREIGN KEY (GroupMemberReceiverID)
	REFERENCES GroupModule.GroupMembers (GroupMemberID)
)
--SELECT * FROM GroupModule.GroupSession
--SELECT * FROM GroupModule.GroupMembers


