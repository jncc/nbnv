DECLARE @datasetKey CHAR(8)
SET @datasetKey = 'GA000466'

BEGIN TRAN

INSERT INTO [NBNCore].[dbo].[Dataset] 
SELECT 
	d.datasetKey 
	, 'T'
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
INNER JOIN [NBNGatewayOrig].[dbo].[TaxonDataset] td ON td.datasetKey = d.datasetKey 
INNER JOIN [NBNGatewayOrig].[dbo].Organisation o ON d.datasetProvider = o.organisationKey 
INNER JOIN [NBNCore].[dbo].Organisation org ON org.name = o.organisationName COLLATE Latin1_General_CI_AS
WHERE d.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[TaxonDataset] 
SELECT 
	td.datasetKey 
	, 1
	, td.allowRecordValidation 
FROM [NBNGatewayOrig].[dbo].TaxonDataset td
WHERE td.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[Survey] (datasetKey, providerKey, title, [description], geographicalCoverage, temporalCoverage)
SELECT 
	su.datasetKey 
	, ISNULL(su.providerKey, 'NBN-' + su.datasetKey + '-' + CAST(su.surveyKey AS VARCHAR))
	, su.surveyTitle 
	, su.[description] 
	, su.geographicalCoverage 
	, su.temporalCoverage 
FROM [NBNGatewayOrig].dbo.Survey su 
WHERE su.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[Sample] (surveyID, providerKey)
SELECT DISTINCT
	sur.id 
	, ('NBN-' + o.datasetKey + '-' + ISNULL(su.providerKey, CAST(o.surveyKey AS VARCHAR)) + '-' + CAST(o.sampleKey AS VARCHAR)) AS sampleKey
FROM [NBNGatewayOrig].dbo.TaxonOccurrence o
INNER JOIN [NBNGatewayOrig].dbo.Survey su ON su.surveyKey = o.surveyKey 
INNER JOIN [NBNCore].dbo.Survey sur ON sur.datasetKey = o.datasetKey COLLATE Latin1_General_CI_AS AND sur.providerKey = ISNULL(su.providerKey, 'NBN-' + su.datasetKey + '-' + CAST(su.surveyKey AS VARCHAR)) COLLATE Latin1_General_CI_AS
WHERE o.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[Site] (datasetKey, name, providerKey)
SELECT DISTINCT
	o.datasetKey 
	, rs.siteName 
	, ISNULL(rs.providerKey, 'NBN-SITE-' + o.datasetKey + '-' + CAST(rs.recordedSiteKey AS VARCHAR))
FROM [NBNGatewayOrig].[dbo].[TaxonOccurrence] o
INNER JOIN [NBNGatewayOrig].[dbo].[RecordedSite] rs ON rs.recordedSiteKey = o.recordedSiteKey 
WHERE o.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[Recorder] (name)
SELECT DISTINCT
	r.recorderName 
FROM [NBNGatewayOrig].[dbo].[TaxonOccurrence] o
INNER JOIN [NBNGatewayOrig].[dbo].[Recorder] r ON r.nameID = o.recorder OR r.nameID = o.determiner 
WHERE r.recorderName COLLATE Latin1_General_CI_AS NOT IN (SELECT nr.name FROM [NBNCore].[dbo].[Recorder] nr)
AND o.datasetKey = @datasetKey

SELECT 
	o.datasetKey COLLATE Latin1_General_CI_AS AS datasetKey
	, ('NBN-' + o.datasetKey + '-' + ISNULL(su.providerKey, CAST(o.surveyKey AS VARCHAR)) + '-' + CAST(o.sampleKey AS VARCHAR)) COLLATE Latin1_General_CI_AS AS sampleKey 
	, ISNULL(o.providerKey, 'NBN-' + o.datasetKey + '-' + ISNULL(su.providerKey, CAST(o.surveyKey AS VARCHAR)) + '-' + CAST(o.sampleKey AS VARCHAR) + '-' + o.taxonOccurrenceKey) COLLATE Latin1_General_CI_AS AS providerKey
	, t.nbnTaxonVersionKey COLLATE Latin1_General_CI_AS AS nbnTaxonVersionKey
	, CASE WHEN o.startYear IS NOT NULL AND o.startYear > 0 THEN CAST(CAST(o.startYear AS varchar) + '-' + CAST(o.startMonth AS varchar) + '-' + CAST(o.startDay AS varchar) AS date) ELSE NULL END AS startDate
	, CASE WHEN o.endYear IS NOT NULL AND o.endYear > 0 THEN CAST(CAST(o.endYear AS varchar) + '-' + CAST(o.endMonth AS varchar) + '-' + CAST(o.endDay AS varchar) AS date) ELSE NULL END AS endDate
	, o.dateTypeKey COLLATE Latin1_General_CI_AS AS dateTypeKey
	, ISNULL(rs.providerKey, 'NBN-SITE-' + o.datasetKey + '-' + CAST(rs.recordedSiteKey AS VARCHAR)) COLLATE Latin1_General_CI_AS AS siteKey
	, ISNULL(ISNULL(ISNULL(tos.GRIDREF100M, tos.GRIDREF1K), tos.GRIDREF2K), tos.GRIDREF10K) COLLATE Latin1_General_CI_AS AS gridRef
	, o.zeroAbundance 
	, o.accessLevel 
	, rr.recorderName COLLATE Latin1_General_CI_AS AS recorder
	, rd.recorderName COLLATE Latin1_General_CI_AS AS determiner
INTO [#records]
FROM [NBNGatewayOrig].[dbo].[TaxonOccurrence] o
INNER JOIN [NBNGatewayOrig].[dbo].[Survey] su ON su.surveyKey = o.surveyKey 
INNER JOIN [NBNGatewayOrig].[dbo].[Taxon] t ON t.taxonVersionKey = o.taxonVersionKey 
INNER JOIN [NBNGatewayOrig].[dbo].[RecordedSite] rs ON rs.recordedSiteKey = o.recordedSiteKey 
INNER JOIN [NBNGatewayOrig].[dbo].[TAXONOCCURRENCESQUARE] tos ON tos.TOSKEY = o.tosKey 
INNER JOIN [NBNGatewayOrig].[dbo].[Recorder] rr ON rr.nameID = o.recorder 
INNER JOIN [NBNGatewayOrig].[dbo].[Recorder] rd ON rd.nameID = o.determiner
WHERE o.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[TaxonObservation] (sampleID, providerKey, taxonVersionKey, dateStart, dateEnd, dateTypeKey, siteID, featureID, absenceRecord, sensitiveRecord, recorderID, determinerID)
SELECT 
	sa.id 
	, r.providerKey
	, r.nbnTaxonVersionKey 
	, r.startDate 
	, r.endDate 
	, r.dateTypeKey 
	, si.id
	, gs.featureID
	, r.zeroAbundance 
	, r.accessLevel 
	, nrr.id
	, nrd.id
FROM [#records] r
INNER JOIN [NBNCore].[dbo].[Sample] sa ON sa.providerKey = r.sampleKey 
INNER JOIN [NBNCore].[dbo].[Survey] su ON su.id = sa.surveyID AND su.datasetKey = r.datasetKey  
INNER JOIN [NBNCore].[dbo].[Site] si ON si.providerKey = r.siteKey AND si.datasetKey = r.datasetKey 
INNER JOIN [NBNCore].[dbo].[GridSquare] gs ON gs.gridRef = r.gridRef 
INNER JOIN [NBNCore].[dbo].[Taxon] t ON t.taxonVersionKey = r.nbnTaxonVersionKey --WARNING: Bad for data health, only here to make it work
INNER JOIN [NBNCore].[dbo].[Recorder] nrr ON nrr.name = r.recorder 
INNER JOIN [NBNCore].[dbo].[Recorder] nrd ON nrd.name = r.determiner 
WHERE r.datasetKey = @datasetKey

SELECT DISTINCT
	toa.title COLLATE Latin1_General_CI_AS AS title 
	, 'NBN-ATTRIB-' + o.datasetKey + '-' + CAST(toa.attributeKey AS VARCHAR) COLLATE Latin1_General_CI_AS AS providerKey
	, toa.[description] COLLATE Latin1_General_CI_AS AS [description]
	, 4 AS storageLevelID
	, 3 AS storageTypeID
INTO [#attributes]
FROM [NBNGatewayOrig].[dbo].[TaxonOccurrence] o
INNER JOIN [NBNGatewayOrig].[dbo].[TaxonOccurrenceAttributeData] toad ON toad.occurrencekey = o.taxonOccurrenceKey 
INNER JOIN [NBNGatewayOrig].[dbo].[TaxonOccurrenceAttribute] toa ON toa.attributeKey = toad.attributekey 
WHERE o.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[Attribute] (label, [description], storageLevelID, storageTypeID)
SELECT
	a.title 
	, a.providerKey 
	, a.storageLevelID 
	, a.storageTypeID 
FROM [#attributes] a

SELECT
	'NBN-ATTRIB-' + o.datasetKey + '-' + CAST(toa.attributeKey AS VARCHAR) COLLATE Latin1_General_CI_AS AS attribProviderKey
	, ISNULL(o.providerKey, 'NBN-' + o.datasetKey + '-' + ISNULL(su.providerKey, CAST(o.surveyKey AS VARCHAR)) + '-' + CAST(o.sampleKey AS VARCHAR) + '-' + o.taxonOccurrenceKey) COLLATE Latin1_General_CI_AS AS obsProviderKey
	, toad.dataValue 
INTO [#attributedata]
FROM [NBNGatewayOrig].[dbo].[TaxonOccurrence] o
INNER JOIN [NBNGatewayOrig].[dbo].[Survey] su ON su.surveyKey = o.surveyKey 
INNER JOIN [NBNGatewayOrig].[dbo].[TaxonOccurrenceAttributeData] toad ON toad.occurrencekey = o.taxonOccurrenceKey 
INNER JOIN [NBNGatewayOrig].[dbo].[TaxonOccurrenceAttribute] toa ON toa.attributeKey = toad.attributekey 
WHERE o.datasetKey = @datasetKey

INSERT INTO [NBNCore].[dbo].[TaxonObservationAttribute] (attributeID, observationID, textValue)
SELECT 
	a.id 
	, obs.id
	, ad.dataValue 
FROM [#attributedata] ad
INNER JOIN [NBNCore].[dbo].[Attribute] a ON a.[description] = ad.attribProviderKey 
INNER JOIN [NBNCore].[dbo].[TaxonObservation] obs ON obs.providerKey = ad.obsProviderKey 
INNER JOIN [NBNCore].[dbo].[Sample] sa ON sa.id = obs.sampleID
INNER JOIN [NBNCore].[dbo].[Survey] su ON su.id = sa.surveyID 
WHERE su.datasetKey = @datasetKey

UPDATE [NBNCore].[dbo].[Attribute] SET [Attribute].[description] = data.[description] 
FROM [Attribute] INNER JOIN [#attributes] data ON data.providerKey = [Attribute].[description]

DROP TABLE [#records]
DROP TABLE [#attributes]
DROP TABLE [#attributedata] 

COMMIT