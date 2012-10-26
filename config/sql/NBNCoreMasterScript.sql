/* 
 *
 * Drop and Create Database
 *
 */

USE [master]
GO
EXEC sp_removedbreplication NBNCore
GO
EXEC msdb.dbo.sp_delete_database_backuphistory @database_name = N'NBNCore'
GO
ALTER DATABASE [NBNCore] SET  SINGLE_USER WITH ROLLBACK IMMEDIATE
GO
DROP DATABASE [NBNCore]
GO

IF NOT EXISTS(SELECT principal_id FROM sys.server_principals WHERE name = 'NBNImporter') 
BEGIN
	CREATE LOGIN [NBNImporter] WITH PASSWORD=N'Ecowaswashere9', DEFAULT_DATABASE=[NBNCore], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
END

GO

--------------------------------

CREATE DATABASE [NBNCore]
 ON  PRIMARY 
( NAME = N'NBNCore', FILENAME = N'd:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\NBNCoreNew.mdf' , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'NBNCore_log', FILENAME = N'd:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\NBNCoreNew_log.ldf' , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [NBNCore] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [NBNCore].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [NBNCore] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [NBNCore] SET ANSI_NULLS ON
GO
ALTER DATABASE [NBNCore] SET ANSI_PADDING OFF
GO
ALTER DATABASE [NBNCore] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [NBNCore] SET ARITHABORT OFF
GO
ALTER DATABASE [NBNCore] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [NBNCore] SET AUTO_CREATE_STATISTICS ON
GO
ALTER DATABASE [NBNCore] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [NBNCore] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [NBNCore] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [NBNCore] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [NBNCore] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [NBNCore] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [NBNCore] SET QUOTED_IDENTIFIER ON
GO
ALTER DATABASE [NBNCore] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [NBNCore] SET  DISABLE_BROKER
GO
ALTER DATABASE [NBNCore] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [NBNCore] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [NBNCore] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [NBNCore] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [NBNCore] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [NBNCore] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [NBNCore] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [NBNCore] SET  READ_WRITE
GO
ALTER DATABASE [NBNCore] SET RECOVERY FULL
GO
ALTER DATABASE [NBNCore] SET  MULTI_USER
GO
ALTER DATABASE [NBNCore] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [NBNCore] SET DB_CHAINING OFF
GO
EXEC sys.sp_db_vardecimal_storage_format N'NBNCore', N'ON'
GO
USE [NBNCore]
GO

/*
 *
 * Organisation
 *
 */

CREATE TABLE [dbo].[Organisation](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [varchar](200) NOT NULL UNIQUE,
	[abbreviation] [varchar](10) NULL,
	[summary] [varchar](max) NULL,
	[address] [varchar](200) NULL,
	[postcode] [varchar](10) NULL,
	[phone] [varchar](50) NULL,
	[website] [varchar](100) NULL,
	[contactName] [varchar](120) NULL,
	[contactEmail] [varchar](100) NULL,
	[allowPublicRegistration] [bit] NOT NULL,
	[logoSmall] [varbinary](max) NULL,
	[logo] [varbinary](max) NULL,
);

/*
 *
 * User
 *
 */

CREATE TABLE [dbo].[UserType](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](100) NOT NULL UNIQUE
);

INSERT INTO [UserType] VALUES 
(1, 'public'),
(2, 'user'),
(3, 'admin');

------------------------------

CREATE TABLE [dbo].[User](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[username] [varchar](40) NOT NULL UNIQUE,
	[password_sha1] [varbinary](8000) NOT NULL,
	[password_md5_sha1] [varbinary](8000) NOT NULL,
	[userTypeID] [int] NOT NULL REFERENCES [UserType] ([id]),
	[forename] [varchar](25) NOT NULL,
	[surname] [varchar](25) NOT NULL,
	[phone] [varchar](15) NULL,
	[email] [varchar](70) NOT NULL UNIQUE,
	[active] [bit] NOT NULL,
	[activationKey] [char](12) NOT NULL,
	[invalidEmail] [bit] NOT NULL,
	[allowEmailAlerts] [bit] NOT NULL,
	[subscribedToAdminMails] [bit] NOT NULL,
	[subscribedToNBNMarketting] [bit] NOT NULL,
	[bannedFromValidation] [bit] NOT NULL,
	[englishNameOrder] [bit] NOT NULL,
	[registrationDate] [datetime] NULL,
	[lastLoggedIn] [datetime] NULL,
);

INSERT INTO [User] (englishNameOrder, lastLoggedIn, forename, surname, username, email, registrationDate, phone, allowEmailAlerts, invalidEmail, active, activationKey, subscribedToNBNMarketting, bannedFromValidation, subscribedToAdminMails, password_sha1, password_md5_sha1, userTypeID) VALUES
(0, NULL, '', '', 'public', 'public@data.nbn.org.uk', CURRENT_TIMESTAMP, NULL, 1, 0, 0, '000000000000', 0, 1, 1, 0x00000000, 0x00000000, 1);

------------------------------

CREATE TABLE [dbo].[UserOrganisationRole](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
);

INSERT INTO [UserOrganisationRole] VALUES
(1, 'member'),
(2, 'administrator'),
(3, 'lead');

------------------------------

CREATE TABLE [dbo].[UserOrganisationMembership](
	[userID] [int] NOT NULL REFERENCES [User] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[organisationID] [int] NOT NULL REFERENCES [Organisation] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[organisationRoleID] [int] NOT NULL REFERENCES [UserOrganisationRole] ([id]),
	PRIMARY KEY ([userID] ASC, [organisationID] ASC)
);

/*
 *
 * Dataset Metadata
 *
 */
 
CREATE TABLE [dbo].[DatasetType](
	[key] [char](1) NOT NULL PRIMARY KEY,
	[label] [varchar](20) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[DatasetType] VALUES
('H', 'Habitat'),
('A', 'Site Boundary'),
('T', 'Taxon');

------------------------------

CREATE TABLE [dbo].[DatasetUpdateFrequency](
	[code] [char](3) NOT NULL PRIMARY KEY,
	[label] [varchar](12) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[DatasetUpdateFrequency] VALUES
('001', 'continual'),
('005', 'monthly'),
('006', 'quarterly'),
('007', 'biannually'),
('008', 'annually'),
('009', 'as needed'),
('010', 'irregular'),
('011', 'not planned'),
('012', 'unknown');

------------------------------

CREATE TABLE [dbo].[Dataset](
	[key] [char](8) NOT NULL PRIMARY KEY,
	[datasetTypeKey] [char](1) NOT NULL REFERENCES [DatasetType] ([key]),
	[providerOrganisationKey] [int] NOT NULL REFERENCES [Organisation] ([id]),
	[title] [varchar](200) NOT NULL,
	[description] [varchar](max) NULL,
	[providerKey] [varchar](500) NULL,
	[dataCaptureMethod] [varchar](max) NULL,
	[purpose] [varchar](max) NULL,
	[geographicalCoverage] [varchar](max) NULL,
	[temporalCoverage] [varchar](max) NULL,
	[dataQuality] [varchar](max) NULL,
	[additionalInformation] [varchar](max) NULL,
	[accessConstraints] [varchar](max) NULL,
	[useConstraints] [varchar](max) NULL,
	[updateFrequencyCode] [char](3) NOT NULL REFERENCES [DatasetUpdateFrequency] ([code]),
	[dateUploaded] [datetime] NOT NULL,
	[metadataLastEdited] [datetime] NOT NULL,
);

------------------------------

CREATE TABLE [dbo].[DatasetKeyword](
	[datasetKey] [char](8) NOT NULL REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[keyword] [varchar](200) NOT NULL,
	[thesaurus] [varchar](500) NOT NULL,
	PRIMARY KEY ([datasetKey] ASC, [keyword] ASC, [thesaurus] ASC)
);

------------------------------

CREATE TABLE [dbo].[DatasetAdministrator](
	[userID] [int] NOT NULL REFERENCES [User] ([id]),
	[datasetKey] [char](8) NOT NULL REFERENCES [Dataset] ([key]),
	PRIMARY KEY ([userID] ASC, [datasetKey] ASC)
);

/*
 *
 * Feature and Geometry
 *
 */

CREATE TABLE [dbo].[Feature](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[geom] [geometry] NOT NULL,
);

CREATE NONCLUSTERED INDEX [idx_Feature_geom] ON [dbo].[Feature] (
	[id] ASC
) INCLUDE ( 
	[geom]
) WITH (
	PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_Feature_geom] ON [dbo].[Feature] (
	[geom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-11, 49, 3, 63), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 

------------------------------

CREATE TABLE [dbo].[FeatureContains](
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	[containedFeatureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	PRIMARY KEY ([featureID] ASC, [containedFeatureID] ASC)
);


------------------------------

CREATE TABLE [dbo].[FeatureOverlaps](
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	[overlappedFeatureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	PRIMARY KEY ([featureID] ASC, [overlappedFeatureID] ASC)
);

------------------------------

GO

-- =============================================
-- Author:		Paul Gilbertson
-- Create date: 20120824
-- Description:	Maintains geometry data in the warehouse tables
-- =============================================
CREATE TRIGGER [dbo].[trg_iud_Feature] 
   ON  [dbo].[Feature] 
   AFTER INSERT,DELETE,UPDATE
AS 
BEGIN
	SET NOCOUNT ON;

	DELETE FROM [FeatureContains] 
	WHERE featureID IN (SELECT id FROM deleted) 
	OR containedFeatureID IN (SELECT id FROM deleted)
	
	INSERT INTO [FeatureContains] (featureID, containedFeatureID)
	SELECT
		i.id
		, f.id
	FROM inserted i
	INNER JOIN [Feature] f on i.geom.STContains(f.geom) = 1
	
	INSERT INTO [FeatureContains] (featureID, containedFeatureID)
	SELECT
		f.id
		, i.id
	FROM inserted i
	INNER JOIN [Feature] f on f.geom.STContains(i.geom) = 1
	WHERE f.id != i.id

	DELETE FROM [FeatureOverlaps] 
	WHERE featureID IN (SELECT id FROM deleted) 
	OR overlappedFeatureID IN (SELECT id FROM deleted)
	
	INSERT INTO [FeatureOverlaps] (featureID, overlappedFeatureID)
	SELECT
		i.id 
		, f.id
	FROM inserted i
	INNER JOIN [Feature] f on i.geom.STIntersects(f.geom) = 1 and i.geom.STTouches(f.geom) = 0
	
	INSERT INTO [FeatureOverlaps] (featureID, overlappedFeatureID)
	SELECT
		f.id
		, i.id
	FROM inserted i
	INNER JOIN [Feature] f on f.geom.STIntersects(i.geom) = 1 and f.geom.STTouches(i.geom) = 0
	WHERE f.id != i.id
END

GO

------------------------------

GO

-- =============================================
-- Author:		Paul Gilbertson
-- Create date: 20120118
-- Description:	Creates a Feature from WKT
-- =============================================
CREATE PROCEDURE [dbo].[import_UpdateFeature]
	@featureID INT,
	@wkt VARCHAR(MAX)
AS
BEGIN
	UPDATE Feature SET geom = geometry::STGeomFromText(@wkt, 4326) WHERE id = @featureID
END

GO

------------------------------

GO

-- =============================================
-- Author:		Paul Gilbertson
-- Create date: 20121004
-- Description:	Creates a Feature from WKT
-- =============================================
CREATE PROCEDURE [dbo].[import_CreateFeature]
	@wkt VARCHAR(MAX)
	,@FeatureId INT OUT
AS
BEGIN
	INSERT INTO Feature (geom) VALUES (geometry::STGeomFromText(@wkt, 4326))
	SET @FeatureId = SCOPE_IDENTITY();
END

GO

------------------------------

CREATE TABLE [dbo].[Resolution](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](20) NOT NULL UNIQUE,
	[accuracy] [int] NULL,
	[area] [int] NULL,
);

INSERT INTO [dbo].[Resolution] VALUES
(-1, 'Polygon', NULL, NULL),
(0, 'None', NULL, NULL),
(1, '10km', 10000, 100000000),
(2, '2km', 2000, 4000000),
(3, '1km', 1000, 1000000),
(4, '100m', 100, 10000);

------------------------------

CREATE TABLE [dbo].[Projection](
	[id] [int] NOT NULL PRIMARY KEY,
	[srcSRID] [int] NOT NULL UNIQUE,
	[label] [varchar](50) NOT NULL UNIQUE
);

INSERT INTO [dbo].[Projection] VALUES
(1, 27700, 'OSGB36'),
(2, 29903, 'OSNI'),
(3, 23030, 'ED50'),
(4, 4326, 'WGS84');

------------------------------

CREATE TABLE [dbo].[GridSquare](
	[gridRef] [varchar](12) NOT NULL PRIMARY KEY,
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[resolutionID] [int] NOT NULL REFERENCES [Resolution] ([id]),
	[parentSquareGridRef] [varchar](12) NULL REFERENCES [GridSquare] ([gridRef]),
	[originalProjectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[originalGeom] [geometry] NOT NULL
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_GridSquare_originalGeom] ON [dbo].[GridSquare] (
	[originalGeom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-11, 49, 3, 63), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 

------------------------------
GO

-- =============================================
-- Author:		Paul Gilbertson
-- Create date: 20120118
-- Description:	Creates a Feature from WKT
-- =============================================
CREATE PROCEDURE [dbo].[import_UpdateGridSquare]
	@featureID INT,
	@wkt VARCHAR(MAX)
AS
BEGIN
	DECLARE @srid INT
	SELECT @srid = srcSRID FROM Projection p INNER JOIN GridSquare gs ON gs.originalProjectionID = p.id WHERE gs.featureID = @featureID 

	UPDATE GridSquare SET originalGeom = geometry::STGeomFromText(@wkt, @srid) WHERE featureID = @featureID
END

GO

------------------------------

GO

-- =============================================
-- Author:		Paul Gilbertson
-- Create date: 20121004
-- Description:	Creates a Feature from WKT
-- =============================================
CREATE PROCEDURE [dbo].[import_CreateGridSquare]
	@featureID INT
	, @gridRef VARCHAR(12)
	, @resolutionID INT
	, @projectionID INT
	, @wkt VARCHAR(MAX)
AS
BEGIN
	DECLARE @srid INT
	SET  @srid = (SELECT srcSRID FROM Projection WHERE id = @projectionID)

	INSERT INTO GridSquare (featureID, gridRef, resolutionID, originalProjectionID, originalGeom) VALUES (@featureID, @gridRef, @resolutionID, @projectionID, geometry::STGeomFromText(@wkt, @srid))	
END

GO

------------------------------

CREATE TABLE [dbo].[GridTree](
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	[parentFeatureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	PRIMARY KEY ([featureID] ASC, [parentFeatureID] ASC)
);

------------------------------

GO

CREATE TRIGGER [dbo].[trg_iud_GridSquare]
   ON [dbo].[GridSquare]
   AFTER INSERT,DELETE,UPDATE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DELETE FROM [dbo].[GridTree] 
	WHERE parentFeatureID IN (SELECT featureID FROM deleted)
	OR featureID IN (SELECT featureID FROM deleted)

	INSERT INTO [dbo].[GridTree] ([featureID], [parentFeatureID])
		SELECT g.featureID, g.featureID
		FROM [dbo].[GridSquare] g
		INNER JOIN inserted i ON i.featureID = g.featureID 
		UNION
		SELECT g.featureID, gp.featureID
		FROM [dbo].[GridSquare] g
		INNER JOIN [dbo].[GridSquare] gp ON gp.gridRef = g.parentSquareGridRef 
		INNER JOIN inserted i ON i.featureID = g.featureID 
		WHERE g.resolutionID >= 2
		UNION 
		SELECT g.featureID, gp.featureID
		FROM [dbo].[GridSquare] g
		INNER JOIN [dbo].[GridSquare] gb ON gb.gridRef = g.parentSquareGridRef 
		INNER JOIN [dbo].[GridSquare] gp ON gp.gridRef = gb.parentSquareGridRef 
		INNER JOIN inserted i ON i.featureID = g.featureID 
		WHERE g.resolutionID >= 3
		UNION
		SELECT g.featureID, gp.featureID
		FROM [dbo].[GridSquare] g
		INNER JOIN [dbo].[GridSquare] gb ON gb.gridRef = g.parentSquareGridRef 
		INNER JOIN [dbo].[GridSquare] gc ON gc.gridRef = gb.parentSquareGridRef 
		INNER JOIN [dbo].[GridSquare] gp ON gp.gridRef = gc.parentSquareGridRef 
		INNER JOIN inserted i ON i.featureID = g.featureID 
		WHERE g.resolutionID = 4
END

GO

/*
 *
 * GridExtents
 *
 */

CREATE TABLE [dbo].[GridExtents] (
	[id] [int] NOT NULL PRIMARY KEY,
	[geom] [geometry] NOT NULL,
	[projectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[priority] [int] NOT NULL UNIQUE
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_GridExtents_geom] ON [dbo].[GridExtents] (
	[geom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-15, 40, 8, 65), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 
/*
 *
 * Habitat
 *
 */

CREATE TABLE [dbo].[HabitatCategory](
	[id] [int] NOT NULL PRIMARY KEY,
	[name] [varchar](max) NOT NULL
);

INSERT INTO [dbo].[HabitatCategory] VALUES
(1, 'BAP Priority Habitat');

------------------------------

CREATE TABLE [dbo].[HabitatDataset](
	[datasetKey] [char](8) NOT NULL PRIMARY KEY REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[habitatCategory] [int] NOT NULL REFERENCES [HabitatCategory] ([id])
);

------------------------------

CREATE TABLE [dbo].[HabitatFeature](
	[featureID] [int] NOT NULL PRIMARY KEY REFERENCES [Feature] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[providerKey] [varchar](100) NOT NULL,
	[habitatDataset] [char](8) NOT NULL REFERENCES [HabitatDataset] ([datasetKey]),
	[originalProjectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[originalGeom] [geometry] NOT NULL,
	[uploadDate] [datetime] NOT NULL
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_HabitatFeature_originalGeom] ON [dbo].[HabitatFeature] (
	[originalGeom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-11, 49, 3, 63), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 

/*
 *
 * Site Boundary
 *
 */

CREATE TABLE [dbo].[SiteBoundaryCategory](
	[id] [int] NOT NULL PRIMARY KEY,
	[name] [varchar](max) NOT NULL,
);

INSERT INTO [dbo].[SiteBoundaryCategory] VALUES
(1, 'Designated and Protected Areas'),
(2, 'Local Wildlife Sites'),
(3, 'Organisation Sites'),
(4, 'Administrative and Landscape');

------------------------------

CREATE TABLE [dbo].[SiteBoundaryType](
	[id] [int] NOT NULL PRIMARY KEY,
	[parentID] [int] NULL REFERENCES [SiteBoundaryType] ([id]),
	[siteTypeName] [varchar](100) NOT NULL,
	[siteTypeCode] [varchar](30) NULL,
	[xmlEnumValue] [varchar](100) NULL,
);

------------------------------

CREATE TABLE [dbo].[SiteBoundaryDataset](
	[datasetKey] [char](8) NOT NULL PRIMARY KEY REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[siteBoundaryCategory] [int] NOT NULL REFERENCES [SiteBoundaryCategory] ([id]),
	[siteBoundaryType] [int] NOT NULL REFERENCES [SiteBoundaryType] ([id]),
	[nameField] [varchar](100) NOT NULL,
);

------------------------------

CREATE TABLE [dbo].[SiteBoundary](
	[featureID] [int] NOT NULL PRIMARY KEY REFERENCES [Feature] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[name] [varchar](200) NOT NULL,
	[description] [varchar](max) NULL,
	[providerKey] [varchar](100) NOT NULL,
	[siteBoundaryDataset] [char](8) NOT NULL REFERENCES [SiteBoundaryDataset] ([datasetKey]),
	[originalProjectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[originalGeom] [geometry] NOT NULL,
	[uploadDate] [datetime] NOT NULL
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_SiteBoundary_originalGeom] ON [dbo].[SiteBoundary] (
	[originalGeom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-11, 49, 3, 63), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 

/*
 *
 * Taxon Dictionary
 *
 */

CREATE TABLE [dbo].[Language](
	[key] [char](2) NOT NULL PRIMARY KEY,
	[name] [varchar](50) NOT NULL UNIQUE,
	[sortOrder] [int] NOT NULL UNIQUE
);

INSERT INTO [Language] VALUES
('cy', 'Welsh', 3),
('en', 'English', 2),
('gd', 'Gaelic', 4),
('la', 'Latin', 1);

------------------------------

CREATE TABLE [dbo].[TaxonRank](
	[id] [int] NOT NULL PRIMARY KEY,
	[rank] [varchar](20) NOT NULL,
	[level] [int] NOT NULL,
);

------------------------------

CREATE TABLE [dbo].[TaxonVersionForm](
	[key] [char](1) NOT NULL PRIMARY KEY,
	[description] [varchar](50) NULL,
	[sortOrder] [int] NOT NULL UNIQUE,
);

INSERT INTO [TaxonVersionForm] VALUES
('W', 'Well-formed', 1),
('U', 'Unverified', 2),
('I', 'Ill-formed', 3);

------------------------------

CREATE TABLE [dbo].[TaxonNameStatus](
	[key] [char](1) NOT NULL PRIMARY KEY,
	[description] [varchar](50) NULL,
);

INSERT INTO [TaxonNameStatus] VALUES
('R', 'Recommended'),
('S', 'Synonym'),
('U', 'Undetermined');

------------------------------

CREATE TABLE [dbo].[Organism](
	[key] [char](16) NOT NULL PRIMARY KEY,
	[parentOrganismKey] [char](16) NULL REFERENCES [Organism] ([key])
);

------------------------------

CREATE TABLE [dbo].[TaxonGroup](
	[key] [char](16) NOT NULL PRIMARY KEY,
	[name] [varchar](50) NULL,
	[description] [varchar](65) NULL,
	[sortOrder] [int] NULL,
	[outputFlag] [bit] NOT NULL,
	[parentTaxonGroupKey] [char](16) NULL REFERENCES [TaxonGroup] ([key]),
);

------------------------------

CREATE TABLE [dbo].[Taxon](
	[taxonVersionKey] [char](16) NOT NULL PRIMARY KEY,
	[pTaxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[organismKey] [char](16) NOT NULL REFERENCES [Organism] ([key]),
	[name] [varchar](181) NOT NULL,
	[authority] [varchar](80) NULL,
	[languageKey] [char](2) NOT NULL REFERENCES [Language] ([key]),
	[commonNameTaxonVersionKey] [char](16) NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[taxonCode] [varchar](5) NULL,
	[taxonRankID] [int] NOT NULL REFERENCES [TaxonRank] ([id]),
	[taxonNameStatusKey] [char](1) NOT NULL REFERENCES [TaxonNameStatus] ([key]),
	[taxonVersionFormKey] [char](1)  NOT NULL REFERENCES [TaxonVersionForm] ([key]),
	[taxonOutputGroupKey] [char](16) NULL REFERENCES [TaxonGroup] ([key])
);

------------------------------

CREATE TABLE [dbo].[TaxonAggregate] (
	[aggregateTaxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[componentTaxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	PRIMARY KEY ([aggregateTaxonVersionKey], [componentTaxonVersionKey])
);

------------------------------

CREATE TABLE [dbo].[RecordingEntity] (
	[recordedName] [varchar](255) NOT NULL PRIMARY KEY,
	[taxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[dangerous] [bit] NOT NULL
);

------------------------------

CREATE TABLE [dbo].[TaxonNavigation](
	[taxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[taxonNavigationGroupKey] [char](16) NOT NULL REFERENCES [TaxonGroup] ([key]),
	PRIMARY KEY ([taxonVersionKey] ASC, [taxonNavigationGroupKey] ASC)
);

/*
 *
 * Taxon Designation
 *
 */

CREATE TABLE [dbo].[DesignationCategory](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
	[description] [varchar](max) NULL,
	[sortOrder] [int] NOT NULL,
);

------------------------------

CREATE TABLE [dbo].[Designation](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [varchar](8000) NOT NULL,
	[label] [varchar](50) NOT NULL UNIQUE,
	[code] [varchar](100) NOT NULL UNIQUE,
	[designationCategoryID] [int] NOT NULL REFERENCES [DesignationCategory] ([id]),
	[description] [varchar](max) NULL,
	[geographicalConstraint] [varchar](max) NULL,
	[featureID] [int] NULL REFERENCES [Feature] ([id]),
);

------------------------------

CREATE TABLE [dbo].[TaxonDesignation](
	[taxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[designationID] [int] NOT NULL REFERENCES [Designation] ([id]),
	[startDate] [datetime] NULL,
	[endDate] [datetime] NULL,
	[source] [varchar](max) NULL,
	[statusConstraint] [varchar](max) NULL,
	PRIMARY KEY ([taxonVersionKey] ASC, [designationID] ASC)
);

/*
 *
 * Taxon Observation
 *
 */

CREATE TABLE [dbo].[TaxonDataset](
	[datasetKey] [char](8) NOT NULL PRIMARY KEY REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[publicResolutionID] [int] NOT NULL REFERENCES [Resolution] ([id]),
	[allowRecordValidation] [bit] NOT NULL,
	[publicAttribute] [bit] NOT NULL
);

------------------------------

CREATE TABLE [dbo].[Survey](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[datasetKey] [char](8) NOT NULL REFERENCES [TaxonDataset] ([datasetKey]),
	[providerKey] [varchar](100) NULL,
	[title] [varchar](200) NULL,
	[description] [varchar](max) NULL,
	[geographicalCoverage] [varchar](max) NULL,
	[temporalCoverage] [varchar](max) NULL
);

------------------------------

CREATE TABLE [dbo].[Sample](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[surveyID] [int] NOT NULL REFERENCES [Survey] ([id]),
	[providerKey] [varchar](100) NULL,
	[title] [varchar](200) NULL,
	[description] [varchar](max) NULL,
	[geographicalCoverage] [varchar](max) NULL,
	[temporalCoverage] [varchar](max) NULL
);

------------------------------

CREATE TABLE [dbo].[DateType](
	[key] [varchar](2) NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL,
);

INSERT INTO [dbo].[DateType] VALUES
('<D', 'Before Date'),
('<Y', 'Before Year'),
('>D', 'After Date'),
('>Y', 'After Year'),
('B', 'Unknown'),
('C', 'Circa'),
('D', 'Day'),
('DD', 'Day Range'),
('M', 'Month'),
('MM', 'Month Range'),
('ND', 'No date'),
('O', 'Month'),
('OO', 'Month Range'),
('P', 'Publication Date'),
('R', 'Unknown'),
('U', 'Unknown'),
('XX', 'Unknown'),
('Y', 'Year'),
('-Y', 'Before Year'),
('Y-', 'After Year'),
('YY', 'Year Range');

------------------------------

CREATE TABLE [dbo].[Recorder](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[name] [varchar](140) NOT NULL,
);

------------------------------

CREATE TABLE [dbo].[Site](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[datasetKey] [char](8) NOT NULL REFERENCES [Dataset] ([key]),
	[name] [varchar](200) NOT NULL,
	[providerKey] [varchar](100) NULL,
);

SET ANSI_PADDING OFF;

CREATE NONCLUSTERED INDEX [idx_Site_id-providerKey-name] ON [dbo].[Site] 
(
	[id] ASC,
	[providerKey] ASC,
	[name] ASC
);

SET ANSI_PADDING ON;

------------------------------

CREATE TABLE [dbo].[TaxonObservation](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[sampleID] [int] NOT NULL REFERENCES [Sample] ([id]),
	[providerKey] [varchar](100) NOT NULL,
	[taxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[dateStart] [date] NULL,
	[dateEnd] [date] NULL,
	[dateTypeKey] [varchar](2) NOT NULL REFERENCES [DateType] ([key]),
	[siteID] [int] NULL REFERENCES [Site] ([id]),
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	[absenceRecord] [bit] NOT NULL,
	[sensitiveRecord] [bit] NOT NULL,
	[recorderID] [int] NULL REFERENCES [Recorder] ([id]),
	[determinerID] [int] NULL REFERENCES [Recorder] ([id])
);

SET ANSI_PADDING OFF;

CREATE NONCLUSTERED INDEX [idx_TaxonObservation_sampleID-id-featureID-TVK] ON [dbo].[TaxonObservation] 
(
	[sampleID] ASC,
	[id] ASC,
	[featureID] ASC,
	[taxonVersionKey] ASC
);

CREATE NONCLUSTERED INDEX [idx_TaxonObservation_sampleID-providerKey] ON [dbo].[TaxonObservation] 
(
	[sampleID] ASC,
	[providerKey] ASC
);

CREATE NONCLUSTERED INDEX [idx_TaxonObservation_taxonVersionKey] ON [dbo].[TaxonObservation] 
(
	[taxonVersionKey] ASC
) INCLUDE ( 
	[siteID]
);

SET ANSI_PADDING ON;

------------------------------

CREATE TABLE [dbo].[TaxonObservationPublic](
	[taxonObservationID] [int] NOT NULL PRIMARY KEY REFERENCES [TaxonObservation] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[siteID] [int] NULL REFERENCES [Site] ([id]),
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]),
	[recorderID] [int] NULL REFERENCES [Recorder] ([id]),
	[determinerID] [int] NULL REFERENCES [Recorder] ([id]),
);

/*
 *
 * Taxon Observation Access
 *
 */

CREATE TABLE [dbo].[UserTaxonObservationAccess](
	[userID] [int] NOT NULL REFERENCES [User] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[observationID] [int] NOT NULL REFERENCES [TaxonObservation] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY ([userID] ASC, [observationID] ASC)
);

------------------------------

CREATE TABLE [dbo].[OrganisationTaxonObservationAccess](
	[organisationID] [int] NOT NULL REFERENCES [Organisation] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[observationID] [int] NOT NULL REFERENCES [TaxonObservation] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY ([organisationID] ASC, [observationID] ASC)
);

/*
 *
 * Attributes
 *
 */

CREATE TABLE [dbo].[AttributeStorageLevel](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](12) NOT NULL UNIQUE,
);

INSERT INTO [AttributeStorageLevel] VALUES
(1, 'dataset'),
(2, 'survey'),
(3, 'sample'),
(4, 'observation'),
(5, 'boundary'),
(6, 'habitat');

------------------------------

CREATE TABLE [dbo].[AttributeStorageType](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](11) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[AttributeStorageType] VALUES
(1, 'decimal'),
(2, 'enumeration'),
(3, 'free text');

------------------------------

CREATE TABLE [dbo].[GatewayAttribute](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
	[description] [varchar](max) NOT NULL,
	[storageTypeID] [int] NOT NULL REFERENCES AttributeStorageType ([id]),
);

------------------------------

CREATE TABLE [dbo].[GatewayAttributeEnumeration](
	[gatewayAttributeID] [int] NOT NULL REFERENCES [GatewayAttribute] ([id]),
	[enumValue] [int] NOT NULL,
	[label] [varchar](50) NOT NULL,
	[description] [varchar](max) NULL,
	PRIMARY KEY ([gatewayAttributeID] ASC, [enumValue] ASC),
	UNIQUE ([gatewayAttributeID], [label])
);

------------------------------

CREATE TABLE [dbo].[Attribute](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL,
	[description] [varchar](max) NOT NULL,
	[storageLevelID] [int] NOT NULL REFERENCES [AttributeStorageLevel] ([id]),
	[storageTypeID] [int] NOT NULL REFERENCES [AttributeStorageType] ([id]),
	[gatewayAttributeID] [int] NULL REFERENCES [GatewayAttribute] ([id]),
);

------------------------------

CREATE TABLE [dbo].[AttributeEnumeration](
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[enumValue] [int] NOT NULL,
	[label] [varchar](50) NOT NULL,
	[description] [varchar](max) NULL,
	PRIMARY KEY ([attributeID] ASC, [enumValue] ASC),
	UNIQUE ([attributeID], [label])
);

------------------------------

CREATE TABLE [dbo].[DatasetAttribute](
	[datasetKey] [char](8) NOT NULL REFERENCES [Dataset] ([key]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([datasetKey] ASC, [attributeID] ASC)
);

-----------------------------

CREATE TABLE [dbo].[TaxonAttribute](
	[taxonVersionKey] [char](16) NOT NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([taxonVersionKey] ASC, [attributeID] ASC)
);

----------------------------

CREATE TABLE [dbo].[SiteBoundaryAttribute](
	[featureID] [int] NOT NULL REFERENCES [SiteBoundary] ([featureID]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([featureID] ASC, [attributeID] ASC)
);

----------------------------

CREATE TABLE [dbo].[HabitatFeatureAttribute](
	[featureID] [int] NOT NULL REFERENCES [HabitatFeature] ([featureID]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([featureID] ASC, [attributeID] ASC)
);

----------------------------

CREATE TABLE [dbo].[SurveyAttribute](
	[surveyID] [int] NOT NULL REFERENCES [Survey] ([id]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([surveyID] ASC, [attributeID] ASC)
);

----------------------------

CREATE TABLE [dbo].[SampleAttribute](
	[sampleID] [int] NOT NULL REFERENCES [Sample] ([id]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([sampleID] ASC, [attributeID] ASC)
);

---------------------------

CREATE TABLE [dbo].[TaxonObservationAttribute](
	[observationID] [int] NOT NULL REFERENCES [TaxonObservation] ([id]),
	[attributeID] [int] NOT NULL REFERENCES [Attribute] ([id]),
	[decimalValue] [decimal](18, 0) NULL,
	[enumValue] [int] NULL,
	[textValue] [varchar](255) NULL,
	PRIMARY KEY ([observationID] ASC, [attributeID] ASC)
);

/*
 *
 * Taxon Observation Filters
 *
 */


CREATE TABLE [dbo].[TaxonObservationFilter](
	[id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[filterJSON] [varchar](max) NOT NULL,
	[filterText] [varchar](max) NOT NULL
);

---------------------------

CREATE TABLE [dbo].[TaxonObservationFilterElementType](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE
);

INSERT INTO [TaxonObservationFilterElementType] VALUES
(1, 'Dataset'),
(2, 'Taxon'),
(3, 'Sensitive'),
(4, 'Site Boundary'),
(5, 'Date');

---------------------------

CREATE TABLE [dbo].[TaxonObservationFilterElement](
	[id] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
	[filterID] [int] NOT NULL REFERENCES [TaxonObservationFilter] ([id]),
	[filterElementTypeID] [int] NOT NULL REFERENCES [TaxonObservationFilterElementType] ([id]),
	[filterDatasetKey] [char](8) NULL REFERENCES [TaxonDataset] ([datasetKey]),
	[filterTaxon] [char](16) NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[filterSensitive] [int] NULL,
	[filterSiteBoundary] [int] NULL REFERENCES [SiteBoundary] ([featureID]),
	[filterSiteBoundaryMatch] [int] NULL,
	[filterDateStart] [date] NULL,
	[filterDateEnd] [date] NULL,
);

---------------------------

CREATE TABLE [dbo].[AccessRequestRole] (
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE
);

---------------------------

CREATE TABLE [dbo].[AccessRequestType] (
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE
);

---------------------------

CREATE TABLE [dbo].[AccessRequestResponseType] (
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE
);

---------------------------

CREATE TABLE [dbo].[UserAccessRequest](
	[filterID] [int] NOT NULL PRIMARY KEY REFERENCES [TaxonObservationFilter] ([id]),
	[userID] [int] NOT NULL REFERENCES [User] ([id]),
	[requestRoleID] [int] NOT NULL REFERENCES [AccessRequestRole] ([id]),
	[requestTypeID] [int] NOT NULL REFERENCES [AccessRequestType] ([id]),
	[requestReason] [varchar](max) NOT NULL,
	[requestDate] [datetime] NOT NULL,
	[responseTypeID] [int] NULL REFERENCES [AccessRequestResponseType] ([id]),
	[responseReason] [varchar](max) NULL,
	[responseDate] [datetime] NULL,
	[accessExpires] [date] NULL
);

---------------------------

CREATE TABLE [dbo].[OrganisationAccessRequest](
	[filterID] [int] NOT NULL PRIMARY KEY REFERENCES [TaxonObservationFilter] ([id]),
	[organisationID] [int] NOT NULL REFERENCES [Organisation] ([id]),
	[requestRoleID] [int] NOT NULL REFERENCES [AccessRequestRole] ([id]),
	[requestTypeID] [int] NOT NULL REFERENCES [AccessRequestType] ([id]),
	[requestReason] [varchar](max) NOT NULL,
	[requestDate] [datetime] NOT NULL,
	[responseTypeID] [int] NULL REFERENCES [AccessRequestResponseType] ([id]),
	[responseReason] [varchar](max) NULL,
	[responseDate] [datetime] NULL,
	[accessExpires] [date] NULL
);

/*
 *
 * Download Log
 *
 */

CREATE TABLE [dbo].[TaxonObservationDownloadPurpose](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[TaxonObservationDownloadPurpose] VALUES 
(1, 'Education purposes'),
(2, 'Research'),
(3, 'Media'),
(4, 'Conservation NGO work'),
(5, 'Commercial and consultancy work'),
(6, 'Statutory work'),
(7, 'Data provision and interpretation services'),
(8, 'Private use');

-------------------------------

CREATE TABLE [dbo].[TaxonObservationDownload](
	[filterID] [int] NOT NULL PRIMARY KEY REFERENCES [TaxonObservationFilter] ([id]),
	[purposeID] [int] NOT NULL REFERENCES [TaxonObservationDownloadPurpose] ([id]),
	[reason] [varchar](max) NOT NULL,
	[downloadTime] [datetime] NOT NULL,
	[userID] [int] NULL REFERENCES [User] ([id]),
	[userForOrganisation] [varchar](max) NULL,
	[organisationID] [int] NULL REFERENCES [Organisation] ([id]),
);

-------------------------------

CREATE TABLE [dbo].[TaxonObservationDownloadStatistics](
	[filterID] [int] NOT NULL REFERENCES [TaxonObservationDownload] ([filterID]),
	[datasetKey] [char](8) NOT NULL REFERENCES [TaxonDataset] ([datasetKey]),
	[recordCount] [int] NOT NULL,
	PRIMARY KEY ([filterID] ASC, [datasetKey] ASC)
);

/*
 *
 * Utility Stored Procedures
 *
 */

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

GO

CREATE PROCEDURE [dbo].[usp_test_DatasetsMissingSubtypeEntry]
AS
BEGIN
	SELECT d.[key], 'Taxon' AS datasetType FROM Dataset d
	WHERE d.datasetTypeKey = 'T' AND d.[key] NOT IN (SELECT td.datasetKey FROM TaxonDataset td)
	UNION ALL
	SELECT d.[key], 'Habitat' AS datasetType FROM Dataset d
	WHERE d.datasetTypeKey = 'H' AND d.[key] NOT IN (SELECT td.datasetKey FROM HabitatDataset td)
	UNION ALL
	SELECT d.[key], 'SiteBoundary' AS datasetType FROM Dataset d
	WHERE d.datasetTypeKey = 'A' AND d.[key] NOT IN (SELECT td.datasetKey FROM SiteBoundaryDataset td)
END

GO

/*
 *
 * Replication Stored Procedures
 *
 */

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

GO

CREATE PROCEDURE [dbo].[usp_repl_AddSubscriber]
	@subscriberHost varchar(255)
	, @subscriberDB varchar(255)
AS
BEGIN
	EXEC sp_addsubscription 
		@publication = N'Warehouse'
		, @subscriber = @subscriberHost
		, @destination_db = @subscriberDB
		, @subscription_type = N'Push'
		, @sync_type = N'automatic'
		, @article = N'all'
		, @update_mode = N'read only'
		, @subscriber_type = 0

	EXEC sp_addpushsubscription_agent 
		@publication = N'Warehouse'
		, @subscriber = @subscriberHost
		, @subscriber_db = @subscriberDB
		, @job_login = N'NBNGATEWAY\replagent'
		, @job_password = N'TentSmokeSurroundedTime2012'
		, @subscriber_security_mode = 1
		, @frequency_type = 64
		, @frequency_interval = 0
		, @frequency_relative_interval = 0
		, @frequency_recurrence_factor = 0
		, @frequency_subday = 0
		, @frequency_subday_interval = 0
		, @active_start_time_of_day = 0
		, @active_end_time_of_day = 235959
		, @active_start_date = 20120821
		, @active_end_date = 99991231
		, @enabled_for_syncmgr = N'False'
		, @dts_package_location = N'Distributor'
END

GO

------------------------------

CREATE PROCEDURE [dbo].[usp_dev_DropFromPublication]
	@article varchar(200)
AS
BEGIN
	exec sp_dropsubscription 
		@publication = N'Warehouse'
		, @article = @article
		, @subscriber = N'all'
		, @destination_db = N'all'
		
	exec sp_droparticle 
		@publication = N'Warehouse'
		, @article = @article
		, @force_invalidate_snapshot = 1
END

GO

------------------------------

CREATE PROCEDURE [dbo].[usp_dev_AddViewToPublicationAsView]
	@view varchar(200)
AS
BEGIN
	exec sp_addarticle 
		@publication = N'Warehouse'
		, @article = @view
		, @source_owner = N'dbo'
		, @source_object = @view
		, @type = N'indexed view schema only'
		, @description = N''
		, @creation_script = N''
		, @pre_creation_cmd = N'drop'
		, @schema_option = 0x0000000008000051
		, @identityrangemanagementoption = N'none'
		, @destination_table = @view
		, @destination_owner = N'dbo'
		, @status = 16
		, @vertical_partition = N'false'
		, @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
		, @force_invalidate_snapshot = 1
END

GO

------------------------------

CREATE PROCEDURE [dbo].[usp_dev_AddViewToPublication]
	@view varchar(200)
AS
BEGIN
	exec sp_addarticle 
		@publication = N'Warehouse'
		, @article = @view
		, @source_owner = N'dbo'
		, @source_object = @view
		, @type = N'indexed view logbased'
		, @description = N''
		, @creation_script = N''
		, @pre_creation_cmd = N'drop'
		, @schema_option = 0x0000000008000051
		, @identityrangemanagementoption = N'none'
		, @destination_table = @view
		, @destination_owner = N'dbo'
		, @status = 16
		, @vertical_partition = N'false'
		, @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
		, @force_invalidate_snapshot = 1
END

GO

------------------------------

CREATE PROCEDURE [dbo].[usp_dev_AddTableToPublication]
	@view varchar(200)
AS
BEGIN
	exec sp_addarticle 
		@publication = N'Warehouse'
		, @article = @view
		, @source_owner = N'dbo'
		, @source_object = @view
		, @type = N'logbased'
		, @description = N''
		, @creation_script = N''
		, @pre_creation_cmd = N'drop'
		, @schema_option = 0x0000000008000051
		, @identityrangemanagementoption = N'none'
		, @destination_table = @view
		, @destination_owner = N'dbo'
		, @status = 16
		, @vertical_partition = N'false'
		, @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
		, @force_invalidate_snapshot = 1
END

GO

------------------------------

CREATE PROCEDURE [dbo].[usp_dev_SnapshotPublication]
AS
BEGIN
	exec sp_startpublication_snapshot
		@publication = N'Warehouse'	
END

GO

------------------------------

-- =============================================
-- Author:  Felix Mason
-- Create date: 12/10/2012
-- Description: Gets the grid system that provides the
-- most appropriate mapping context for a geometry.
-- =============================================
CREATE PROCEDURE [dbo].[import_getGridForWKT]
	@wkt varchar(max)
	, @projection varchar(50) OUT
AS
BEGIN
	DECLARE @geom AS geometry
	SET @geom = geometry::STGeomFromText(@wkt, 4326)
 
	-- get the grid system with the highest priority.
	SET @projection = (
		SELECT TOP 1 
			p.srcSRID 
		FROM [dbo].[Projection] p
		INNER JOIN [dbo].[GridExtents] ge ON ge.projectionID = p.id
		WHERE ge.geom.STIntersects(@geom) = 1
		ORDER BY ge.[priority] desc
	)
END

GO

------------------------------

CREATE PROCEDURE [dbo].[import_SiteBoundaryData]
	@datasetKey CHAR(8)
	, @layer VARCHAR(255)
	, @keyColumn VARCHAR(255)
	, @nameColumn VARCHAR(255)
	, @category INT
	, @srid INT
	, @loadDate DATETIME2
AS
BEGIN
	DECLARE @projection INT

	SET @projection = (SELECT p.id FROM [NBNCore].[dbo].[Projection] p WHERE p.srcSRID = @srid)

	CREATE TABLE [#slayer] (
		[id] int
		, [originalGeom] geometry
		, [geom] geometry
		, [providerKey] varchar(100)
		, [siteName] varchar(200)
	)
		
	DECLARE @createTempTable VARCHAR(8000)
	
	SET @createTempTable = 'INSERT INTO [#slayer] SELECT orig.ogr_fid AS id, orig.ogr_geometry AS originalGeom, wgs.ogr_geometry AS geom, orig.[' + @keyColumn + '] AS providerKey, orig.[' + @nameColumn + '] AS siteName FROM [NBNSpatial].[dbo].[' + @layer + '] orig INNER JOIN [NBNSpatial].[dbo].[' + @layer + '_wgs] wgs ON wgs.ogr_fid = orig.ogr_fid'
	EXEC @createTempTable 

	DECLARE CUR CURSOR FOR SELECT id FROM [#slayer] 
	DECLARE @pid INT

	OPEN CUR
	FETCH CUR INTO @pid

	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO [NBNCore].[dbo].Feature (geom)
		SELECT 
			sl.geom 
		FROM [#slayer] sl
		WHERE sl.id = @pid 
		
		INSERT INTO [NBNCore].[dbo].SiteBoundary (featureID, siteBoundaryDataset, name, providerKey, originalProjectionID, originalGeom, uploadDate)
		SELECT
			@@IDENTITY
			, @datasetKey
			, sl.siteName 
			, sl.providerKey 
			, @projection
			, sl.originalGeom 
			, @loadDate
		FROM [#slayer] sl
		WHERE sl.id = @pid 
		
		FETCH NEXT FROM CUR INTO @pid
	END

	CLOSE CUR
	DEALLOCATE CUR

	DROP TABLE [#slayer]
	
	DECLARE @cName varchar(255)
	
	DECLARE COLCUR CURSOR FOR
	SELECT 
		c.COLUMN_NAME 
	FROM information_schema.columns c
	WHERE c.TABLE_NAME = @layer 
	AND COLUMN_NAME NOT IN ('ogr_fid', 'ogr_geometry', 'objectid', 'adminsitekey')
	ORDER BY ORDINAL_POSITION 
	
	CREATE TABLE [#sAttrib] (
		featureID int
		, attributeID int
		, data varchar(255)
	)
	
	OPEN COLCUR
	FETCH COLCUR INTO @cName
	
	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO [NBNCore].[dbo].[Attribute] (label, [description], storageLevelID, storageTypeID)
		VALUES (@cName, 'NBN-ATTRIB-' + @layer + '-' + @cName, 5, 3)
		
		SET @createTempTable = 'INSERT INTO [NBNCore].[dbo].[SiteBoundaryAttribute] (featureID, attributeID, textValue) SELECT sb.featureID, a.id, CAST(l.[' + @cName + '] AS varchar FROM [NBNSpatial].[dbo].[' + @layer + '] l INNER JOIN [NBNCore].[dbo].[SiteBoundary] sb ON sb.providerKey = l.[' + @keyColumn + '] AND sb.siteBoundaryDataset = ''' + @datasetKey + ''' INNER JOIN [NBNCore].[dbo].[Attribute] a ON a.[description] = ''NBN-ATTRIB-' + @layer + '-' + @cName + ''''
		EXEC @createTempTable 

		FETCH NEXT FROM COLCUR INTO @cName
	END
	
	CLOSE COLCUR
	DEALLOCATE COLCUR
END

GO

------------------------------

CREATE PROCEDURE [dbo].[import_HabitatFeatureData]
	@datasetKey CHAR(8)
	, @layer VARCHAR(255)
	, @keyColumn VARCHAR(255)
	, @category INT
	, @srid INT
	, @loadDate DATETIME2
AS
BEGIN
	DECLARE @projection INT

	SET @projection = (SELECT p.id FROM [NBNCore].[dbo].[Projection] p WHERE p.srcSRID = @srid)

	CREATE TABLE [#slayer] (
		[id] int
		, [originalGeom] geometry
		, [geom] geometry
		, [providerKey] varchar(100)
	)
		
	DECLARE @createTempTable VARCHAR(8000)
	
	SET @createTempTable = 'INSERT INTO [#slayer] SELECT orig.ogr_fid AS id, orig.ogr_geometry AS originalGeom, wgs.ogr_geometry AS geom, orig.[' + @keyColumn + '] AS providerKey FROM [NBNSpatial].[dbo].[' + @layer + '] orig INNER JOIN [NBNSpatial].[dbo].[' + @layer + '_wgs] wgs ON wgs.ogr_fid = orig.ogr_fid'
	EXEC @createTempTable 

	DECLARE CUR CURSOR FOR SELECT id FROM [#slayer] 
	DECLARE @pid INT

	OPEN CUR
	FETCH CUR INTO @pid

	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO [NBNCore].[dbo].Feature (geom)
		SELECT 
			sl.geom 
		FROM [#slayer] sl
		WHERE sl.id = @pid 
		
		INSERT INTO [NBNCore].[dbo].HabitatFeature (featureID, habitatDataset, providerKey, originalProjectionID, originalGeom, uploadDate)
		SELECT
			@@IDENTITY
			, @datasetKey
			, sl.siteName 
			, sl.providerKey 
			, @projection
			, sl.originalGeom 
			, @loadDate
		FROM [#slayer] sl
		WHERE sl.id = @pid 
		
		FETCH NEXT FROM CUR INTO @pid
	END

	CLOSE CUR
	DEALLOCATE CUR

	DROP TABLE [#slayer]
	
	DECLARE @cName varchar(255)
	
	DECLARE COLCUR CURSOR FOR
	SELECT 
		c.COLUMN_NAME 
	FROM information_schema.columns c
	WHERE c.TABLE_NAME = @layer 
	AND COLUMN_NAME NOT IN ('ogr_fid', 'ogr_geometry', 'objectid', 'adminsitekey')
	ORDER BY ORDINAL_POSITION 
	
	CREATE TABLE [#sAttrib] (
		featureID int
		, attributeID int
		, data varchar(255)
	)
	
	OPEN COLCUR
	FETCH COLCUR INTO @cName
	
	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO [NBNCore].[dbo].[Attribute] (label, [description], storageLevelID, storageTypeID)
		VALUES (@cName, 'NBN-ATTRIB-' + @layer + '-' + @cName, 5, 3)
		
		SET @createTempTable = 'INSERT INTO [NBNCore].[dbo].[HabitatFeatureAttribute] (featureID, attributeID, textValue) SELECT sb.featureID, a.id, CAST(l.[' + @cName + '] AS varchar FROM [NBNSpatial].[dbo].[' + @layer + '] l INNER JOIN [NBNCore].[dbo].[HabitatFeature] hf ON hf.providerKey = l.[' + @keyColumn + '] AND hf.habitatDataset = ''' + @datasetKey + ''' INNER JOIN [NBNCore].[dbo].[Attribute] a ON a.[description] = ''NBN-ATTRIB-' + @layer + '-' + @cName + ''''
		EXEC @createTempTable 

		FETCH NEXT FROM COLCUR INTO @cName
	END
	
	CLOSE COLCUR
	DEALLOCATE COLCUR
END

GO

------------------------------

CREATE USER [NBNImporter] FOR LOGIN [NBNImporter]
GO
ALTER USER [NBNImporter] WITH DEFAULT_SCHEMA=[dbo]
GO
GRANT SELECT,UPDATE,INSERT,DELETE,EXECUTE TO [NBNImporter]
GO
