CREATE TABLE Request_ResponseType
(
	ResponseTypeID int NOT NULL AUTO_INCREMENT
	, Name varchar(10) NOT NULL
	, CONSTRAINT PK_Request_ResponseTypeID PRIMARY KEY CLUSTERED (ResponseTypeID)
);

--SELECT * FROM Request_ResponseType
--DROP TABLE Request_ResponseType