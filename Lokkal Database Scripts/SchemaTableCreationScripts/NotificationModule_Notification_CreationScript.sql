CREATE SCHEMA NotificationModule;
GO
CREATE TABLE NotificationModule.Notification
(
	NotificationTypeID int IDENTITY (1,1) NOT NULL
	, Name varchar(255) NOT NULL
	CONSTRAINT PK_Notification_NotificationTypeID PRIMARY KEY CLUSTERED (NotificationTypeID)
);
GO

--SELECT * FROM NotificationModule.Notification


