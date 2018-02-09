CREATE SCHEMA ColorModule;
GO
CREATE TABLE ColorModule.Color
(
	ColorID int IDENTITY (1,1) NOT NULL
	, Name varchar(10) NOT NULL
	CONSTRAINT PK_Color_ColorID PRIMARY KEY CLUSTERED (ColorID)
);
GO


--SELECT * FROM ColorModule.Color


