USE [NBNCore]
GO

SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET NUMERIC_ROUNDABORT OFF;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

GO

/*
 *
 * Taxon Group Views
 *
 */

CREATE VIEW [dbo].[TaxonOutputGroupData] WITH SCHEMABINDING AS (
    SELECT
        tg.[key]
		, tg.sortOrder
		, tg.outputFlag
		, tg.name
		, tg.[description]
		, tg.parentTaxonGroupKey
    FROM [dbo].[TaxonGroup] tg
    WHERE outputFlag = 1
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonOutputGroupData_key] ON [dbo].[TaxonOutputGroupData] 
(
	[key] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonOutputGroupData';

GO

CREATE VIEW [dbo].[TaxonNavigationGroupData] WITH SCHEMABINDING AS (
	SELECT 
		tg.[key]
		, tg.sortOrder
		, tg.outputFlag
		, tg.name
		, tg.[description]
		, tg.parentTaxonGroupKey 
	FROM [dbo].[TaxonGroup] tg
	WHERE tg.outputFlag = 0
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonNavigationGroupData_key] ON [dbo].[TaxonNavigationGroupData] 
(
	[key] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonNavigationGroupData';

GO

CREATE VIEW [dbo].[TaxonNavigationData] WITH SCHEMABINDING AS (
	SELECT 
		tn.[taxonVersionKey]
		, tn.[taxonNavigationGroupKey]
	FROM [dbo].[TaxonNavigation] tn
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonNavigationData_taxonVersionKey-taxonNavigationGroupKey] ON [dbo].[TaxonNavigationData] 
(
	[taxonVersionKey] ASC
	, [taxonNavigationGroupKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonNavigationData';

GO

CREATE VIEW [dbo].[TaxonNavigationGroupTaxonCount] WITH SCHEMABINDING AS (
		SELECT 
			tn.[taxonNavigationGroupKey]
			, COUNT_BIG(*) AS taxonCount
		FROM [dbo].[Taxon] t
		INNER JOIN [dbo].[TaxonNavigation] tn ON tn.[taxonVersionKey] = t.taxonVersionKey
		WHERE t.pTaxonVersionKey = t.taxonVersionKey
		GROUP BY tn.[taxonNavigationGroupKey]
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonNavigationGroupTaxonCount_taxonNavigationGroupKey] ON [dbo].[TaxonNavigationGroupTaxonCount] 
(
	[taxonNavigationGroupKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonNavigationGroupTaxonCount';

GO

/*
 *
 * User Views
 *
 */

CREATE VIEW [dbo].[UserData] WITH SCHEMABINDING AS (
	SELECT
		u.id
		, u.forename
		, u.surname
		, u.username 
		, u.email 
		, u.registrationDate 
		, u.phone 
		, ut.label AS userType
	FROM [dbo].[User] u
	INNER JOIN [dbo].[UserType] ut ON ut.id = u.userTypeID 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_UserData_id] ON [dbo].[UserData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'UserData';

GO

CREATE VIEW [dbo].[UserTokenAuthenticationData] WITH SCHEMABINDING AS (
	SELECT 
		u.id
		, HASHBYTES('SHA1', u.username) AS username_sha1
		, u.password_sha1
		, u.password_md5_sha1
	FROM [dbo].[User] u
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_UserTokenAuthenticationData_id] ON [dbo].[UserTokenAuthenticationData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'UserTokenAuthenticationData';

GO

CREATE VIEW [dbo].[OrganisationData] WITH SCHEMABINDING AS (
	SELECT 
		o.id
		, o.name 
		, o.abbreviation 
		, o.summary 
		, o.[address]
		, o.postcode
		, o.phone 
		, o.contactName 
		, o.contactEmail 
		, o.website 
		, o.allowPublicRegistration  
		, o.logo
		, o.logoSmall
	FROM [dbo].[Organisation] o
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_OrganisationData_organisationID] ON [dbo].[OrganisationData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'OrganisationData';

GO

CREATE VIEW [dbo].[OrganisationMembershipData] WITH SCHEMABINDING AS (
	SELECT
		uom.userID 
		, uom.organisationID
		, uor.label AS [role]
	FROM [dbo].[UserOrganisationMembership] uom
	INNER JOIN [dbo].[UserOrganisationRole] uor ON uor.id = uom.organisationRoleID 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_OrganisationMembershipData_userKey-organisationID] ON [dbo].[OrganisationMembershipData] 
(
	[userID] ASC,
	[organisationID] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'OrganisationMembershipData';

GO

/*
 *
 * Feature Views
 *
 */

CREATE VIEW [dbo].[SiteBoundaryCategoryData] WITH SCHEMABINDING AS (
	SELECT
		sbc.id
		, sbc.name
	FROM [dbo].[SiteBoundaryCategory] sbc
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteBoundaryCategoryData_id] ON [dbo].[SiteBoundaryCategoryData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteBoundaryCategoryData';

GO

CREATE VIEW [dbo].[SiteBoundaryData] WITH SCHEMABINDING AS (
	SELECT
		sb.featureID
		, sb.name
		, sb.providerKey
		, sb.[description] 
		, sb.uploadDate 
		, sb.siteBoundaryDataset AS siteBoundaryDatasetKey
		, sbd.siteBoundaryCategory AS siteBoundaryCategoryID
	FROM [dbo].[SiteBoundary] sb
	INNER JOIN [dbo].[SiteBoundaryDataset] sbd ON sbd.datasetKey = sb.siteBoundaryDataset
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteBoundaryData_featureID] ON [dbo].[SiteBoundaryData] 
(
	[featureID] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteBoundaryData';

GO

CREATE VIEW [dbo].[SiteBoundaryFeatureData] WITH SCHEMABINDING AS (
	SELECT
		f.id 
		, sb.siteBoundaryDataset + sb.providerKey AS identifier
		, sb.name AS label
		, f.geom 
		, sb.originalGeom
		, p.label AS originalProjection
	FROM [dbo].[Feature] f
	INNER JOIN [dbo].[SiteBoundary] sb ON f.id = sb.featureID 
	INNER JOIN [dbo].[Projection] p ON p.id = sb.originalProjectionID 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteBoundaryFeatureData_featureID] ON [dbo].[SiteBoundaryFeatureData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteBoundaryFeatureData';

GO

CREATE VIEW [dbo].[HabitatFeatureData] WITH SCHEMABINDING AS (
	SELECT
		hf.featureID
		, hf.providerKey
		, hf.uploadDate 
		, hf.habitatDataset AS habitatDatasetKey
		, hd.habitatCategory AS habitatCategoryID
	FROM [dbo].[HabitatFeature] hf
	INNER JOIN [dbo].[HabitatDataset] hd ON hd.datasetKey = hf.habitatDataset
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_HabitatFeatureData_featureID] ON [dbo].[HabitatFeatureData] 
(
	[featureID] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'HabitatFeatureData';

GO

CREATE VIEW [dbo].[HabitatFeatureFeatureData] WITH SCHEMABINDING AS (
	SELECT
		f.id 
		, hf.habitatDataset + hf.providerKey AS identifier
		, hf.providerKey AS label
		, f.geom 
		, hf.originalGeom
		, p.label AS originalProjection
	FROM [dbo].[Feature] f
	INNER JOIN [dbo].[HabitatFeature] hf ON f.id = hf.featureID 
	INNER JOIN [dbo].[Projection] p ON p.id = hf.originalProjectionID 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_HabitatFeatureFeatureData_featureID] ON [dbo].[HabitatFeatureFeatureData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'HabitatFeatureFeatureData';

GO

CREATE VIEW [dbo].[GridSquareFeatureData] WITH SCHEMABINDING AS (
	SELECT
		f.id 
		, gs.gridRef AS identifier
		, gs.gridRef AS label
		, f.geom 
		, gs.originalGeom
		, p.label AS originalProjection
		, gs.resolutionID 
		, r.label AS resolution
		, gs.parentSquareGridRef 
	FROM [dbo].[Feature] f
	INNER JOIN [dbo].[GridSquare] gs ON f.id = gs.featureID 
	INNER JOIN [dbo].[Projection] p ON p.id = gs.originalProjectionID 
	INNER JOIN [dbo].[Resolution] r ON r.id = gs.resolutionID 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_GridSquareFeatureData_featureID] ON [dbo].[GridSquareFeatureData] 
(
	[id] ASC
);

GO
 
CREATE NONCLUSTERED INDEX [idx_GridSquareFeatureData_resolutionID_featureID-label-geom] ON [dbo].[GridSquareFeatureData] 
(
	[resolutionID] ASC
) INCLUDE ( 
	[id],
	[label],
	[geom]
);

GO

--EXEC usp_dev_AddViewToPublication 'GridSquareFeatureData';

GO

CREATE VIEW [dbo].[FeatureData] WITH SCHEMABINDING AS (
	SELECT
		gsfd.id 
		, gsfd.identifier 
		, gsfd.label 
		, gsfd.geom 
		, gsfd.originalGeom 
		, gsfd.originalProjection 
		, gsfd.resolutionID 
		, 'GridSquare' AS [type]
	FROM [dbo].[GridSquareFeatureData] gsfd
	UNION ALL
	SELECT 
		sbfd.id
		, sbfd.identifier
		, sbfd.label 
		, sbfd.geom 
		, sbfd.originalGeom 
		, sbfd.originalProjection 
		, -1 AS resolutionID
		, 'SiteBoundary' AS [type]
	FROM [dbo].[SiteBoundaryFeatureData] sbfd
	UNION ALL
	SELECT 
		hffd.id
		, hffd.identifier
		, hffd.label 
		, hffd.geom 
		, hffd.originalGeom 
		, hffd.originalProjection 
		, -1 AS resolutionID
		, 'HabitatFeature' AS [type]
	FROM [dbo].[HabitatFeatureFeatureData] hffd
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'FeatureData';

GO

--EXEC usp_dev_AddTableToPublication 'GridTree';

GO

--EXEC usp_dev_AddTableToPublication 'FeatureContains';

GO

--EXEC usp_dev_AddTableToPublication 'FeatureOverlaps';

GO

/*
 *
 * Taxon Data
 *
 */

CREATE VIEW [dbo].[TaxonData] WITH SCHEMABINDING AS (
	SELECT
		t.taxonVersionKey
		, t.pTaxonVersionKey
		, t.name
		, t.authority
		, t.languageKey
		, t.taxonOutputGroupKey
	FROM [dbo].[Taxon] t
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonData_taxonVersionKey] ON [dbo].[TaxonData] 
(
	[taxonVersionKey] ASC
);

GO

CREATE NONCLUSTERED INDEX [idx_TaxonData_prefnameTaxonVersionKey] ON [dbo].[TaxonData] 
(
	[pTaxonVersionKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonData';

GO

/*
 *
 * Designation Views
 *
 */

CREATE VIEW [dbo].[DesignationCategoryData] WITH SCHEMABINDING AS (
	SELECT 
		dc.id
		, dc.label
		, dc.[description]
		, dc.sortOrder 
	FROM [dbo].[DesignationCategory] dc
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DesignationCategoryData_id] ON [dbo].[DesignationCategoryData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DesignationCategoryData';

GO

CREATE VIEW [dbo].[DesignationData] WITH SCHEMABINDING AS (
	SELECT 
		d.id
		, d.name
		, d.label
		, d.code
		, d.designationCategoryID
		, d.[description] 
	FROM [dbo].[Designation] d
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DesignationData_designationID] ON [dbo].[DesignationData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DesignationData';

GO

CREATE VIEW [dbo].[DesignationTaxonData] WITH SCHEMABINDING AS (
	SELECT
		td.designationID
		, d.code
		, t.pTaxonVersionKey
	FROM [dbo].[TaxonDesignation] td
	INNER JOIN [dbo].[Designation] d ON d.id = td.designationID
	INNER JOIN [dbo].[Taxon] t ON t.taxonVersionKey = td.taxonVersionKey AND t.pTaxonVersionKey = td.taxonVersionKey
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DesignationTaxonData_designationID-pTaxonVersionKey] ON [dbo].[DesignationTaxonData] 
(
	[designationID] ASC,
	[pTaxonVersionKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DesignationTaxonData';

GO

CREATE VIEW [dbo].[DesignationTaxonNavigationGroupData] WITH SCHEMABINDING AS (
	SELECT 
		dtd.designationID
		, tngd.[key]
		, tngd.sortOrder
		, tngd.name
		, tngd.[description]
		, tngd.parentTaxonGroupKey 
		, COUNT(DISTINCT t.pTaxonVersionKey) numSpecies
	FROM [dbo].[DesignationTaxonData] dtd 
	INNER JOIN [dbo].[TaxonData] t ON dtd.pTaxonVersionKey = t.taxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationData] tn ON tn.taxonVersionKey = t.pTaxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngd ON tn.taxonNavigationGroupKey = tngd.[key]  
	WHERE tngd.parentTaxonGroupKey IS NULL 
	GROUP BY dtd.designationID, tngd.[key], tngd.sortOrder, tngd.name, tngd.[description], tngd.parentTaxonGroupKey 
	UNION ALL 
	--Get top level groups matched to species through their children
	SELECT 
		dtd.designationID
		, tngd.[key]
		, tngd.sortOrder
		, tngd.name
		, tngd.[description]
		, tngd.parentTaxonGroupKey 
		, COUNT(DISTINCT t.pTaxonVersionKey) numSpecies
	FROM [dbo].[DesignationTaxonData] dtd 
	INNER JOIN [dbo].[TaxonData] t ON dtd.pTaxonVersionKey = t.taxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationData] tn ON tn.taxonVersionKey = t.pTaxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngdp ON tn.taxonNavigationGroupKey = tngdp.[key]  
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngd ON tngd.[key] = tngdp.parentTaxonGroupKey 
	GROUP BY dtd.designationID, tngd.[key], tngd.sortOrder, tngd.name, tngd.[description], tngd.parentTaxonGroupKey 
	UNION ALL
	--Get all children
	SELECT 
		dtd.designationID
		, tngd.[key]
		, tngd.sortOrder
		, tngd.name
		, tngd.[description]
		, tngd.parentTaxonGroupKey 
		, COUNT(DISTINCT t.pTaxonVersionKey) numSpecies
	FROM [dbo].[DesignationTaxonData] dtd 
	INNER JOIN [dbo].[TaxonData] t ON dtd.pTaxonVersionKey = t.taxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationData] tn ON tn.taxonVersionKey = t.pTaxonVersionKey 
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngd ON tn.taxonNavigationGroupKey = tngd.[key]  
	WHERE tngd.parentTaxonGroupKey IS NOT NULL 
	GROUP BY dtd.designationID, tngd.[key], tngd.sortOrder, tngd.name, tngd.[description], tngd.parentTaxonGroupKey 
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'DesignationTaxonNavigationGroupData';

GO

/*
 *
 * Dataset Views
 *
 */

CREATE VIEW [dbo].[DatasetData] WITH SCHEMABINDING AS (
	SELECT
		d.[key]
		, d.title
		, d.[description] 
		, dt.label AS typeName
		, o.name AS organisationName 
		, o.id AS organisationID 
		, d.dataCaptureMethod AS captureMethod
		, d.purpose 
		, d.geographicalCoverage 
		, d.dataQuality AS quality
		, d.additionalInformation 
		, d.accessConstraints 
		, d.useConstraints 
		, d.dateUploaded 
		, d.metadataLastEdited
		, d.temporalCoverage
		, duf.label AS updateFrequency
	FROM [dbo].[Dataset] d
	INNER JOIN [dbo].[Organisation] o ON o.id = d.providerOrganisationKey 
	INNER JOIN [dbo].[DatasetType] dt ON dt.[key] = d.datasetTypeKey 
	INNER JOIN [dbo].[DatasetUpdateFrequency] duf ON duf.code = d.updateFrequencyCode 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DatasetData_datasetKey] ON [dbo].[DatasetData] 
(
	[key] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DatasetData';

GO

CREATE VIEW [dbo].[SiteBoundaryDatasetData] WITH SCHEMABINDING AS (
	SELECT
		sbd.datasetKey 
		, d.title
		, d.[description]
		, d.dateUploaded 
		, o.name AS organisationName
		, o.id AS organisationID
		, sbd.nameField 
		, sbd.siteBoundaryCategory
		, sbc.name AS siteBoundaryCategoryName
	FROM [dbo].[SiteBoundaryDataset] sbd
	INNER JOIN [dbo].[Dataset] d ON d.[key] = sbd.datasetKey 
	INNER JOIN [dbo].[SiteBoundaryCategory] sbc ON sbc.id = sbd.siteBoundaryCategory 
	INNER JOIN [dbo].[Organisation] o ON o.id = d.providerOrganisationKey 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteBoundaryDatasetData_datasetKey] ON [dbo].[SiteBoundaryDatasetData] 
(
	[datasetKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteBoundaryDatasetData'

GO

CREATE VIEW [dbo].[HabitatDatasetData] WITH SCHEMABINDING AS (
	SELECT
		hd.datasetKey 
		, d.title
		, d.[description]
		, d.dateUploaded 
		, o.name AS organisationName
		, o.id AS organisationID
		, hd.habitatCategory
		, hc.name AS habitatCategoryName
	FROM [dbo].[HabitatDataset] hd
	INNER JOIN [dbo].[Dataset] d ON d.[key] = hd.datasetKey 
	INNER JOIN [dbo].[HabitatCategory] hc ON hc.id = hd.habitatCategory 
	INNER JOIN [dbo].[Organisation] o ON o.id = d.providerOrganisationKey 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_HabitatDatasetData_datasetKey] ON [dbo].[HabitatDatasetData] 
(
	[datasetKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'HabitatDatasetData'

GO

CREATE VIEW [dbo].[TaxonDatasetData] WITH SCHEMABINDING AS (
	SELECT 
		su.datasetKey
		, COUNT_BIG(*) AS recordCount
	FROM [dbo].[TaxonObservation] tob 
	INNER JOIN [dbo].[Sample] sa on tob.sampleID = sa.id
	INNER JOIN [dbo].[Survey] su ON sa.surveyID = su.id
	INNER JOIN [dbo].[Taxon] t on tob.taxonVersionKey = t.taxonVersionKey
	GROUP BY su.datasetKey
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonDatasetData_datasetKey] ON [dbo].[TaxonDatasetData] 
(
	[datasetKey] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonDatasetData'

GO

CREATE VIEW [dbo].[DatasetDateTypeRecordCountData] WITH SCHEMABINDING AS (
	SELECT 
		su.datasetKey
		, dt.label AS dateTypeName
		, COUNT_BIG(*) AS recordCount 
	FROM [dbo].[Survey] su 
	INNER JOIN [dbo].[Sample] sa ON su.id = sa.surveyID 
	INNER JOIN [dbo].[TaxonObservation] tob ON sa.id = tob.sampleID 
	INNER JOIN [dbo].[DateType] dt ON dt.[key] = tob.dateTypeKey 
	GROUP BY su.datasetKey, dt.label
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DatasetDateTypeRecordCountData_datasetKey-dateTypeName] ON [dbo].[DatasetDateTypeRecordCountData] 
(
	[datasetKey] ASC,
	[dateTypeName] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DatasetDateTypeRecordCountData'

GO

CREATE VIEW [dbo].[DatasetYearRecordCountData] WITH SCHEMABINDING AS (
	SELECT 
		su.datasetKey
		, ISNULL(YEAR(tob.dateStart), 0) AS [year]
		, COUNT_BIG(*) AS recordCount
	FROM [dbo].[Survey] su 
	INNER JOIN [dbo].[Sample] sa ON su.id = sa.surveyID 
	INNER JOIN [dbo].[TaxonObservation] tob ON sa.id = tob.sampleID 
	WHERE tob.dateStart IS NOT NULL
	GROUP BY su.datasetKey, ISNULL(YEAR(tob.dateStart), 0)
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_DatasetYearRecordCountData_datasetKey] ON [dbo].[DatasetYearRecordCountData] 
(
	[datasetKey] ASC,
	[year] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'DatasetYearRecordCountData'

GO

/*
 *
 * Taxon Observation Views
 *
 */

CREATE VIEW [dbo].[RecorderData] WITH SCHEMABINDING AS (
	SELECT
		r.id
		, r.name
	FROM [dbo].[Recorder] r
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_RecorderData_id] ON [dbo].[RecorderData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'RecorderData'

GO

CREATE VIEW [dbo].[SiteData] WITH SCHEMABINDING AS (
	SELECT
		si.id
		, si.name
		, si.providerKey
	FROM [dbo].[Site] si
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteData_id] ON [dbo].[SiteData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteData'

GO

CREATE VIEW [dbo].[AttributeData] WITH SCHEMABINDING AS (
	SELECT 
		a.id
		, a.label
		, a.[description]
	FROM [dbo].[Attribute] a 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_AttributeData_id] ON [dbo].[AttributeData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'AttributeData'

GO

CREATE VIEW [dbo].[SurveyData] WITH SCHEMABINDING AS (
	SELECT 
		su.id 
		, su.datasetKey
		, su.providerKey 
		, su.title
		, su.[description]
		, su.geographicalCoverage
		, su.temporalCoverage
	FROM [dbo].[Survey] su
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SurveyData_datasetKey-id] ON [dbo].[SurveyData] 
(
	[datasetKey] ASC,
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SurveyData'

GO

CREATE VIEW [dbo].[SampleData] WITH SCHEMABINDING AS (
    SELECT
        s.id
		, s.providerKey 
		, s.surveyID
		, s.[description]
		, s.geographicalCoverage
		, s.temporalCoverage
    FROM [dbo].[Sample] s
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SampleData_id] ON [dbo].[SampleData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SampleData'

GO

CREATE VIEW [dbo].[TaxonObservationAttributeData] WITH SCHEMABINDING AS (
	SELECT 
		toa.observationID
		, toa.attributeID
		, toa.textValue
	FROM [dbo].[TaxonObservationAttribute] toa 
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonObservationAttributeData_obsId_attributeID] ON [dbo].[TaxonObservationAttributeData] 
(
	[observationID] ASC,
	[attributeID] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonObservationAttributeData'

GO

CREATE VIEW [dbo].[TaxonObservationDataPublic] WITH SCHEMABINDING AS (
	SELECT 
		tob.id
        , d.[key] AS datasetKey
        , su.providerKey AS surveyKey
        , sa.providerKey AS sampleKey
        , tob.providerKey AS observationKey
        , obs.siteID 
        , obs.featureID
        , tob.taxonVersionKey
        , t.pTaxonVersionKey
        , tob.dateStart AS startDate
        , tob.dateEnd AS endDate
        , tob.dateTypeKey
        , obs.recorderID 
        , obs.determinerID 
        , tob.sensitiveRecord AS sensitive
        , tob.absenceRecord AS absence
	FROM [dbo].[TaxonObservationPublic] obs 
	INNER JOIN [dbo].[TaxonObservation] tob ON tob.id = obs.taxonObservationID 
	INNER JOIN [dbo].[Taxon] t ON t.taxonVersionKey = tob.taxonVersionKey 
	INNER JOIN [dbo].[Sample] sa ON sa.id = tob.sampleID
	INNER JOIN [dbo].[Survey] su ON su.id = sa.surveyID
    INNER JOIN [dbo].[Dataset] d ON d.[key] = su.datasetKey
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonObservationDataPublic_id] ON [dbo].[TaxonObservationDataPublic] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonObservationDataPublic'

GO

CREATE VIEW [dbo].[TaxonObservationDataEnhanced] WITH SCHEMABINDING AS (
	SELECT 
		obs.id
        , d.[key] AS datasetKey
        , su.providerKey AS surveyKey
        , sa.providerKey AS sampleKey
        , obs.providerKey AS observationKey
        , obs.siteID 
        , obs.featureID
        , obs.taxonVersionKey
        , t.pTaxonVersionKey
        , obs.dateStart AS startDate
        , obs.dateEnd AS endDate
        , obs.dateTypeKey
        , obs.recorderID 
        , obs.determinerID 
        , obs.sensitiveRecord AS sensitive
        , obs.absenceRecord AS absence
	FROM [dbo].[TaxonObservation] obs 
	INNER JOIN [dbo].[Taxon] t ON t.taxonVersionKey = obs.taxonVersionKey
	INNER JOIN [dbo].[Sample] sa ON sa.id = obs.sampleID
	INNER JOIN [dbo].[Survey] su ON su.id = sa.surveyID
    INNER JOIN [dbo].[Dataset] d ON d.[key] = su.datasetKey
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_TaxonObservationDataEnhanced_id] ON [dbo].[TaxonObservationDataEnhanced] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'TaxonObservationDataEnhanced'

GO

CREATE VIEW [dbo].[TaxonObservationData] WITH SCHEMABINDING AS (
	SELECT 
        obs.id
        , 1 AS fullVersion
        , obs.datasetKey
        , obs.surveyKey
        , obs.sampleKey
        , obs.observationKey
        , si.providerKey AS siteKey
        , si.name AS siteName
        , obs.featureID
        , gs.label AS gridRef
        , sb.providerKey AS polygonKey
        , gs.originalProjection
        , obs.taxonVersionKey
        , obs.pTaxonVersionKey
        , pt.name AS pTaxonName
        , pt.authority AS pTaxonAuthority
        , obs.startDate
        , obs.endDate
        , obs.dateTypeKey
        , rr.name AS recorder
        , rd.name AS determiner
        , obs.sensitive
        , obs.absence
    FROM [dbo].[TaxonObservationDataEnhanced] obs 
    INNER JOIN [dbo].[TaxonData] pt ON pt.taxonVersionKey = obs.pTaxonVersionKey
    LEFT JOIN [dbo].[GridSquareFeatureData] gs ON gs.id  = obs.featureID
    LEFT JOIN [dbo].[SiteBoundaryData] sb ON sb.featureID = obs.featureID
    LEFT JOIN [dbo].[SiteData] si ON si.id = obs.siteID
    LEFT JOIN [dbo].[RecorderData] rr ON rr.id = obs.recorderID
    LEFT JOIN [dbo].[RecorderData] rd ON rd.id = obs.determinerID
	UNION ALL
	SELECT 
        obs.id
        , 0 AS fullVersion
        , obs.datasetKey
        , obs.surveyKey
        , obs.sampleKey
        , obs.observationKey
        , si.providerKey AS siteKey
        , si.name
        , obs.featureID
        , gs.label AS gridRef
        , sb.providerKey AS polygonKey
        , gs.originalProjection 
        , obs.taxonVersionKey
        , obs.pTaxonVersionKey
        , pt.name AS pTaxonName
        , pt.authority AS pTaxonAuthority
        , obs.startDate
        , obs.endDate
        , obs.dateTypeKey
        , rr.name AS recorder
        , rd.name AS determiner
        , obs.sensitive
        , obs.absence
    FROM [dbo].[TaxonObservationDataPublic] obs 
    INNER JOIN [dbo].[TaxonData] pt ON pt.taxonVersionKey = obs.pTaxonVersionKey
    LEFT JOIN [dbo].[GridSquareFeatureData] gs ON gs.id = obs.featureID
    LEFT JOIN [dbo].[SiteBoundaryData] sb ON sb.featureID = obs.featureID
    LEFT JOIN [dbo].[SiteData] si ON si.id = obs.siteID
    LEFT JOIN [dbo].[RecorderData] rr ON rr.id = obs.recorderID
    LEFT JOIN [dbo].[RecorderData] rd ON rd.id = obs.determinerID
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'TaxonObservationData'

GO

--EXEC usp_dev_AddTableToPublication 'UserTaxonObservationAccess'

GO

--EXEC usp_dev_AddTableToPublication 'OrganisationTaxonObservationAccess'

GO

CREATE VIEW [dbo].[UserTaxonObservationID] WITH SCHEMABINDING AS (
	SELECT 
		ua.userID
		, ua.observationID
	FROM [dbo].[UserTaxonObservationAccess] ua
	UNION ALL
	SELECT
		omd.userID
		, oa.observationID 
	FROM [dbo].[OrganisationMembershipData] omd
	INNER JOIN [dbo].[OrganisationTaxonObservationAccess] oa ON oa.organisationID = omd.organisationID 
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'UserTaxonObservationID'

GO

CREATE VIEW [dbo].[OrganisationTaxonObservationID] WITH SCHEMABINDING AS (
	SELECT 
		oa.organisationID 
		, oa.observationID
	FROM [dbo].[OrganisationTaxonObservationAccess] oa
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'OrganisationTaxonObservationID'

GO

CREATE VIEW [dbo].[OrganisationPublicTaxonObservationID] WITH SCHEMABINDING AS (
	SELECT 
		org.id AS organisationID
		, o.id AS observationID
	FROM [dbo].[OrganisationData] org
	CROSS JOIN [dbo].[TaxonObservationDataPublic] o
	WHERE o.id NOT IN (
		SELECT 
			oa.observationID 
		FROM [dbo].[OrganisationTaxonObservationAccess] oa
		WHERE oa.organisationID = org.id
	)
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'OrganisationPublicTaxonObservationID'

GO

CREATE VIEW [dbo].[UserPublicTaxonObservationID] WITH SCHEMABINDING AS (
	SELECT 
		u.id AS userID
		, o.id AS observationID
	FROM [dbo].[UserData] u
	CROSS JOIN [dbo].[TaxonObservationDataPublic] o
	WHERE o.id NOT IN (
		SELECT 
			uoi.observationID 
		FROM [dbo].[UserTaxonObservationID] uoi
		WHERE uoi.userID = u.id
	)
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'UserPublicTaxonObservationID'

GO

/****** Object:  View [dbo].[UserTaxonObservationData]    Script Date: 09/19/2012 09:38:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[UserTaxonObservationData] WITH SCHEMABINDING AS (
	SELECT 
		vupoi.userID
		, obs.id AS observationID
		, obs.fullVersion
		, obs.datasetKey
		, obs.surveyKey
		, obs.sampleKey
		, obs.observationKey
		, obs.siteKey
		, obs.siteName
		, obs.featureID
		, obs.gridRef
		, obs.polygonKey
		, obs.originalProjection
		, obs.taxonVersionKey
		, obs.pTaxonVersionKey
		, obs.pTaxonName
		, obs.pTaxonAuthority
		, obs.startDate
		, obs.endDate
		, obs.dateTypeKey
		, obs.recorder
		, obs.determiner
		, obs.sensitive
		, obs.absence
	FROM [dbo].[TaxonObservationData] obs
	INNER JOIN [dbo].[UserPublicTaxonObservationID] vupoi ON vupoi.observationID = obs.id 
	WHERE obs.fullVersion = 0
	UNION ALL
	SELECT 
		vuoi.userID
		, obs.id AS observationID
		, obs.fullVersion
		, obs.datasetKey
		, obs.surveyKey
		, obs.sampleKey
		, obs.observationKey
		, obs.siteKey
		, obs.siteName
		, obs.featureID
		, obs.gridRef
		, obs.polygonKey
		, obs.originalProjection 
		, obs.taxonVersionKey
		, obs.pTaxonVersionKey
		, obs.pTaxonName
		, obs.pTaxonAuthority
		, obs.startDate
		, obs.endDate
		, obs.dateTypeKey 
		, obs.recorder
		, obs.determiner
		, obs.sensitive
		, obs.absence
	FROM [dbo].[TaxonObservationData] obs
	INNER JOIN [dbo].[UserTaxonObservationID] vuoi ON vuoi.observationID = obs.id
	WHERE obs.fullVersion = 1
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'UserTaxonObservationData'

GO

CREATE VIEW [dbo].[OrganisationTaxonObservationData] WITH SCHEMABINDING AS (
	SELECT 
		vopoi.organisationID
		, obs.id AS observationID
		, obs.fullVersion
		, obs.datasetKey
		, obs.surveyKey
		, obs.sampleKey
		, obs.observationKey
		, obs.siteKey
		, obs.siteName
		, obs.featureID
		, obs.gridRef
		, obs.polygonKey
		, obs.originalProjection 
		, obs.taxonVersionKey
		, obs.pTaxonVersionKey
		, obs.pTaxonName
		, obs.pTaxonAuthority
		, obs.startDate
		, obs.endDate
		, obs.dateTypeKey 
		, obs.recorder
		, obs.determiner
		, obs.sensitive
		, obs.absence
	FROM [dbo].[TaxonObservationData] obs
	INNER JOIN [dbo].[OrganisationPublicTaxonObservationID] vopoi ON vopoi.observationID = obs.id
	WHERE obs.fullVersion = 0
	UNION ALL
	SELECT 
		vooi.organisationID
		, obs.id AS observationID
		, obs.fullVersion
		, obs.datasetKey
		, obs.surveyKey
		, obs.sampleKey
		, obs.observationKey
		, obs.siteKey
		, obs.siteName
		, obs.featureID
		, obs.gridRef
		, obs.polygonKey
		, obs.originalProjection 
		, obs.taxonVersionKey
		, obs.pTaxonVersionKey
		, obs.pTaxonName
		, obs.pTaxonAuthority
		, obs.startDate
		, obs.endDate
		, obs.dateTypeKey
		, obs.recorder
		, obs.determiner
		, obs.sensitive
		, obs.absence
	FROM [dbo].[TaxonObservationData] obs
	INNER JOIN [dbo].[OrganisationTaxonObservationID] vooi ON vooi.observationID = obs.id
	WHERE obs.fullVersion = 1
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'OrganisationTaxonObservationData'

GO

CREATE VIEW [dbo].[TaxonDatasetTaxonData] WITH SCHEMABINDING AS (
	SELECT 
		tode.datasetKey 
		, tode.pTaxonVersionKey
		, COUNT_BIG(*) AS observationCount
	FROM [dbo].[TaxonObservationDataEnhanced] tode
	GROUP BY tode.datasetKey, tode.pTaxonVersionKey 
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'TaxonDatasetTaxonData'

GO

CREATE VIEW [dbo].[SurveySpeciesRecordCountData] WITH SCHEMABINDING AS (
	SELECT 
		a.datasetKey
		, a.id
		, a.providerKey 
		, a.title
		, a.[description]
		, a.geographicalCoverage
		, a.temporalCoverage
		, b.speciesCount
		, b.sampleCount
		, b.recordCount 
	FROM [dbo].[SurveyData] a 
	INNER JOIN (
		SELECT 
			obs.datasetKey
			, obs.surveyKey
			, COUNT_BIG(DISTINCT obs.pTaxonVersionKey) AS speciesCount
			, COUNT_BIG(DISTINCT obs.sampleKey) AS sampleCount
			, COUNT_BIG(DISTINCT obs.id) AS recordCount 
		FROM [dbo].[TaxonObservationDataEnhanced] obs
		GROUP BY obs.datasetKey, obs.surveyKey
	) b ON a.providerKey = b.surveyKey AND a.datasetKey = b.datasetKey
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'SurveySpeciesRecordCountData'

GO

CREATE VIEW [dbo].[DatasetAttributeData] WITH SCHEMABINDING AS (
	SELECT DISTINCT 
		tod.datasetKey
		, ad.id AS attributeID
		, ad.label
		, ad.[description]
	FROM [dbo].[TaxonObservationDataEnhanced] tod
	INNER JOIN [dbo].[TaxonObservationAttributeData] toad ON tod.id = toad.observationID
	INNER JOIN [dbo].[AttributeData] ad ON toad.attributeID = ad.id
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'DatasetAttributeData'