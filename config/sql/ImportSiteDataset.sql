USE [NBNCore]
GO

DECLARE @category INT
DECLARE @srid INT
DECLARE @layer VARCHAR(100)

--REMEMBER: Alter the select query into #slayer below
SET @layer = 'VICE_COUNTIES'
SET @category = 1
SET @srid = 27700

DECLARE @datasetKey CHAR(8)
DECLARE @projection INT
DECLARE @loadDate DATETIME2

SET @datasetKey = (SELECT ad.datasetKey FROM [NBNGatewayOrig].[dbo].[AdminDataset] ad WHERE ad.layerName = @layer)
SET @projection = (SELECT p.id FROM [NBNCore].[dbo].[Projection] p WHERE p.srcSRID = @srid)

-----------------------ALTER ME-------------------------
SELECT
	orig.ogr_fid AS id
	, orig.ogr_geometry AS originalGeom
	, wgs.ogr_geometry AS geom
	, orig.vcnumber  AS providerKey
	, orig.vcname AS siteName
INTO [#slayer]
FROM [NBNSpatial].[dbo].[vice_counties] orig 
INNER JOIN [NBNSpatial].[dbo].[vice_counties_wgs] wgs ON wgs.ogr_fid = orig.ogr_fid 


INSERT INTO [NBNCore].[dbo].[Dataset] 
SELECT 
	d.datasetKey 
	, 'A'
	, org.id
	, d.datasetTitle 
	, d.[description] 
	, d.providerKey 
	, d.dataCaptureMethod 
	, d.purpose 
	, d.geographicalCoverage 
	, d.temporalCoverage 
	, d.dataQuality 
	, d.additionalInformation 
	, d.accessConstraints
	, d.useConstraints 
	, '012'
	, d.dateUploaded 
	, ISNULL(d.metadataLastEdited, d.dateUploaded)
FROM [NBNGatewayOrig].[dbo].[Dataset] d
INNER JOIN [NBNGatewayOrig].[dbo].[AdminDataset] ad ON ad.datasetKey = d.datasetKey 
INNER JOIN [NBNGatewayOrig].[dbo].Organisation o ON d.datasetProvider = o.organisationKey 
INNER JOIN [NBNCore].[dbo].Organisation org ON org.name = o.organisationName COLLATE Latin1_General_CI_AS
WHERE d.datasetKey = @datasetKey

SET @loadDate = (SELECT ISNULL(d.dateUploaded, CURRENT_TIMESTAMP) FROM [NBNCore].[dbo].[Dataset] d WHERE d.[key] = @datasetKey)

INSERT INTO [NBNCore].[dbo].[SiteBoundaryDataset] 
SELECT 
	ad.datasetKey 
	, @category
	, sbt.id 
	, ad.nameField 
FROM [NBNGatewayOrig].[dbo].AdminDataset ad
INNER JOIN [NBNGatewayOrig].[dbo].AdminSiteLevel2 asl ON asl.asl2Key = ad.asl2Key 
INNER JOIN [NBNCore].[dbo].[SiteBoundaryType] sbt ON sbt.xmlEnumValue = asl.xmlEnumValue COLLATE Latin1_General_CI_AS
WHERE ad.datasetKey = @datasetKey

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