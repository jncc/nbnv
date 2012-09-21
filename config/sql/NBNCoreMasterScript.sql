/* 
 *
 * Drop and Create Database
 *
 */

USE [master]
GO
EXEC msdb.dbo.sp_delete_database_backuphistory @database_name = N'NBNCore'
GO
ALTER DATABASE [NBNCore] SET  SINGLE_USER WITH ROLLBACK IMMEDIATE
GO
DROP DATABASE [NBNCore]
GO

--------------------------------

CREATE DATABASE [NBNCore]
 CONTAINMENT = NONE
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

/*
 * 
 * Enumeration and Vocabulary Tables
 *
 */

USE [NBNCore]
GO
-------------------------------

CREATE TABLE [dbo].[DownloadLogPurpose](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[DownloadLogPurpose] VALUES 
(0, 'Private use'),
(1, 'Education purposes'),
(2, 'Research'),
(3, 'Media'),
(4, 'Conservation NGO work'),
(5, 'Commercial and consultancy work'),
(6, 'Statutory work'),
(7, 'Data provision and interpretation services');

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

CREATE TABLE [dbo].[AttributeStorageType](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](11) NOT NULL UNIQUE,
);

INSERT INTO [dbo].[AttributeStorageType] VALUES
(0, 'decimal'),
(1, 'enumeration'),
(2, 'free text');

/*
 *
 * Organisation
 *
 */

CREATE TABLE [dbo].[Organisation](
	[id] [int] IDENTITY(0,1) NOT NULL PRIMARY KEY,
	[name] [varchar](200) NOT NULL UNIQUE,
	[abbreviation] [varchar](10) NULL UNIQUE,
	[summary] [varchar](max) NULL,
	[address] [varchar](200) NULL,
	[postcode] [varchar](10) NULL,
	[phone] [varchar](50) NULL,
	[website] [varchar](100) NULL,
	[contactName] [varchar](120) NULL,
	[contactEmail] [varchar](100) NULL,
	[allowPublicRegistration] [bit] NOT NULL,
	[logoSmall] [varchar](max) NULL,
	[logo] [varchar](max) NULL,
);

/*
 *
 * User
 *
 */

CREATE TABLE [dbo].[User](
	[id] [int] IDENTITY(0,1) NOT NULL PRIMARY KEY,
	[username] [varchar](40) NOT NULL UNIQUE,
	[password_sha1] [varbinary](8000) NOT NULL,
	[password_md5_sha1] [varbinary](8000) NOT NULL,
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

INSERT INTO [User] (englishNameOrder, lastLoggedIn, forename, surname, username, email, registrationDate, phone, allowEmailAlerts, invalidEmail, active, activationKey, subscribedToNBNMarketting, bannedFromValidation, subscribedToAdminMails, password_sha1, password_md5_sha1) VALUES
(0, NULL, '', '', 'public', 'public@data.nbn.org.uk', '2012-02-02 14:40:53.427', NULL, 1, 0, 0, '000000000000', 0, 1, 1, 0x00000000, 0x00000000);

------------------------------

CREATE TABLE [dbo].[UserOrganisationRole](
	[id] [int] NOT NULL PRIMARY KEY,
	[label] [varchar](50) NOT NULL UNIQUE,
);

INSERT INTO [UserOrganisationRole] VALUES
(0, 'member'),
(1, 'administrator'),
(2, 'lead');

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

/*
 *
 * Feature and Geometry
 *
 */

CREATE TABLE [dbo].[Feature](
	[id] [int] IDENTITY(0,1) NOT NULL PRIMARY KEY,
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

	DELETE FROM [FeatureOverlaps] 
	WHERE featureID IN (SELECT id FROM deleted) 
	OR overlappedFeatureID IN (SELECT id FROM deleted)
	
	INSERT INTO [FeatureOverlaps] (featureID, overlappedFeatureID)
	SELECT
		i.id 
		, f.id
	FROM inserted i
	INNER JOIN [Feature] f on i.geom.STIntersects(f.geom) = 1
	
	INSERT INTO [FeatureOverlaps] (featureID, overlappedFeatureID)
	SELECT
		f.id
		, i.id
	FROM inserted i
	INNER JOIN [Feature] f on f.geom.STIntersects(i.geom) = 1
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
-- Create date: 20120118
-- Description:	Creates a Feature from WKT
-- =============================================
CREATE PROCEDURE [dbo].[import_CreateFeature]
	@wkt VARCHAR(MAX)
AS
BEGIN
	INSERT INTO Feature (geom) VALUES (geometry::STGeomFromText(@wkt, 4326))
	SELECT @@IDENTITY AS [featureID]
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
(0, 4326, 'WGS84'),
(1, 27700, 'OSGB36'),
(2, 29903, 'OSNI'),
(3, 23030, 'ED50');

------------------------------

CREATE TABLE [dbo].[GridSquare](
	[gridRef] [varchar](12) NOT NULL PRIMARY KEY,
	[featureID] [int] NOT NULL REFERENCES [Feature] ([id]) ON UPDATE CASCADE ON DELETE CASCADE,
	[resolutionID] [int] NOT NULL REFERENCES [Resolution] ([id]),
	[parentSquareGridRef] [varchar](12) NULL REFERENCES [GridSquare] ([gridRef]),
	[projectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[geom] [geometry] NOT NULL
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_GridSquare_geom] ON [dbo].[GridSquare] (
	[geom]
) USING GEOMETRY_GRID WITH (
	BOUNDING_BOX =(-11, 49, 3, 63), GRIDS =(LEVEL_1 = MEDIUM,LEVEL_2 = MEDIUM,LEVEL_3 = MEDIUM,LEVEL_4 = MEDIUM), CELLS_PER_OBJECT = 16, PAD_INDEX  = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON
);

SET ANSI_PADDING OFF; 

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
 * Habitat
 *
 */

CREATE TABLE [dbo].[HabitatDataset](
	[datasetKey] [char](8) NOT NULL PRIMARY KEY REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[geoLayerName] [varchar](50) NULL,
	[gisLayerID] [int] NULL,
);


/*
 *
 * Site Boundary
 *
 */

CREATE TABLE [dbo].[SiteBoundaryCategory](
	[id] [int] NOT NULL PRIMARY KEY,
	[name] [varchar](max) NOT NULL,
);

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
	[geoLayerName] [varchar](100) NOT NULL,
	[gisLayerID] [int] NULL,
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
	[projectionID] [int] NOT NULL REFERENCES [Projection] ([id]),
	[geom] [geometry] NOT NULL,
	[uploadDate] [datetime] NOT NULL
);

SET ANSI_PADDING ON; 

CREATE SPATIAL INDEX [sidx_SiteBoundary_geom] ON [dbo].[SiteBoundary] (
	[geom]
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

------------------------------

CREATE TABLE [dbo].[Organism](
	[key] [char](16) NOT NULL PRIMARY KEY,
	[parentOrganismKey] [char](16) NOT NULL REFERENCES [Organism] ([key])
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
	[pTaxonVersionKey] [char](16) NULL REFERENCES [Taxon] ([taxonVersionKey]),
	[organismKey] [char](16) NOT NULL REFERENCES [Organism] ([key]),
	[scientificName] [varchar](85) NULL,
	[authority] [varchar](80) NULL,
	[languageKey] [char](2) NOT NULL REFERENCES [Language] ([key]),
	[commonName] [varchar](85) NULL,
	[taxonRankID] [int] NULL REFERENCES [TaxonRank] ([id]),
	[taxonVersionFormKey] [char](1) REFERENCES [TaxonVersionForm] ([key]),
	[taxonOutputGroupKey] [char](16) NULL REFERENCES [TaxonGroup] ([key]),
	[taxonNavigationGroupKey] [char](16) NULL REFERENCES [TaxonGroup] ([key]),
);

/*
 *
 * Taxon Observation
 *
 */

CREATE TABLE [dbo].[TaxonDataset](
	[datasetKey] [char](8) NOT NULL PRIMARY KEY REFERENCES [Dataset] ([key]) ON UPDATE CASCADE ON DELETE CASCADE,
	[maxResolutionID] [int] NOT NULL REFERENCES [Resolution] ([id]),
	[publicResolutionID] [int] NOT NULL REFERENCES [Resolution] ([id]),
	[allowRecordValidation] [bit] NOT NULL,
);

------------------------------


