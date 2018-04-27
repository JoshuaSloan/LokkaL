CREATE TABLE GroupModule_GroupSession
(
	GroupID int NOT NULL AUTO_INCREMENT
	, GroupName varchar(50) NOT NULL
	, GroupCreatorID INT NOT NULL
	, StartTime datetime NOT NULL
	, EndTime datetime NULL
	, Active BIT NULL
	, CONSTRAINT PK_Group_GroupID PRIMARY KEY CLUSTERED (GroupID)
	, CONSTRAINT FK_Person_PersonID FOREIGN KEY (GroupCreatorID)
	REFERENCES PersonModule_Person (PersonID)
);

CREATE TABLE GroupModule_GroupMembers
(
	GroupMemberID INT NOT NULL AUTO_INCREMENT
	, GroupID int NOT NULL
	, PersonID INT NOT NULL
	, StartTime datetime NOT NULL
	, EndTime datetime NULL
	, ColorID INT NULL
	, ResponseTypeID INT NOT NULL
	, Active BIT NULL
	, CONSTRAINT PK_GroupMember_GroupMemberID PRIMARY KEY (GroupMemberID)
    , CONSTRAINT FK_GroupMember_PersonID FOREIGN KEY (PersonID)
	REFERENCES PersonModule_Person (PersonID)
	, CONSTRAINT FK_Group_GroupID FOREIGN KEY (GroupID)
	REFERENCES GroupModule_GroupSession (GroupID)
	, CONSTRAINT FK_Color_ColorID FOREIGN KEY (ColorID)
	REFERENCES ColorModule_Color (ColorID)
	, CONSTRAINT FK_GRequest_ResponseTypeID FOREIGN KEY (ResponseTypeID)
	REFERENCES Request_ResponseType (ResponseTypeID)
);


CREATE TABLE GroupModule_GroupMemberLocation
(
	GroupMemberLocationID INT NOT NULL AUTO_INCREMENT
	,GroupMemberID INT NOT NULL
	,LocationTime datetime NOT NULL
	,Latitude DECIMAL(9,6) NOT NULL 
	,Longitude DECIMAL(9,6) NOT NULL 
	,BatteryLife DECIMAL(9,6) NOT NULL
	, Active BIT NULL
	, CONSTRAINT PK_GroupMemberLocation_GroupMemberLocationID PRIMARY KEY CLUSTERED (GroupMemberLocationID)
    , CONSTRAINT FK_GroupMember_MemberID FOREIGN KEY (GroupMemberID)
	REFERENCES GroupModule_GroupMembers (GroupMemberID)
);


CREATE TABLE GroupModule_GroupNotification
(
	GroupNotificationID INT NOT NULL AUTO_INCREMENT
	, NotificationTypeID INT NOT NULL
	, GroupMemberSenderID INT NOT NULL
	, GroupMemberReceiverID INT NOT NULL
	, TimeSent datetime NOT NULL
	, Active bit NULL
	, CONSTRAINT PK_GroupMemberNotification_GroupNotificationID PRIMARY KEY CLUSTERED (GroupNotificationID)
    , CONSTRAINT FK_NotificationType_NotificationTypeID FOREIGN KEY (NotificationTypeID)
	REFERENCES NotificationModule_Notification (NotificationTypeID)
    , CONSTRAINT FK_GroupMember_SenderMemberID FOREIGN KEY (GroupMemberSenderID)
	REFERENCES GroupModule_GroupMembers (GroupMemberID)
    , CONSTRAINT FK_GroupMember_ReceiverMemberID FOREIGN KEY (GroupMemberReceiverID)
	REFERENCES GroupModule_GroupMembers (GroupMemberID)
);
--SELECT * FROM GroupModule_GroupSession;
--SELECT * FROM GroupModule_GroupMembers;
--SELECT * FROM GroupModule_GroupMemberLocation;
--SELECT * FROM GroupModule_GroupNotification;
--DROP TABLE GroupModule_GroupNotification;
--DROP TABLE GroupModule_GroupMemberLocation;
--DROP TABLE GroupModule_GroupMembers;
--DROP TABLE GroupModule_GroupSession;


