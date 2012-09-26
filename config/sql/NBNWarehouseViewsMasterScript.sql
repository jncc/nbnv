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

CREATE VIEW [dbo].[TaxonNavigationGroupTaxonCount] WITH SCHEMABINDING AS (
		SELECT 
			tg.[key] AS taxonNavigationGroupKey
			, COUNT_BIG(*) AS taxonCount
		FROM [dbo].[Taxon] t
		INNER JOIN [dbo].[TaxonGroup] tg ON tg.[key] = t.taxonNavigationGroupKey
		WHERE t.pTaxonVersionKey = t.taxonVersionKey
			AND t.taxonNavigationGroupKey IS NOT NULL
		GROUP BY tg.[key]
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
		, sb.name AS label
		, f.geom 
		, sb.geom AS originalGeom
		, p.label AS originalProjection
	FROM [dbo].[Feature] f
	INNER JOIN [dbo].[SiteBoundary] sb ON f.id = sb.featureID 
	INNER JOIN [dbo].[Projection] p ON p.id = sb.projectionID
);

GO

CREATE UNIQUE CLUSTERED INDEX [cidx_SiteBoundaryFeatureData_featureID] ON [dbo].[SiteBoundaryFeatureData] 
(
	[id] ASC
);

GO

--EXEC usp_dev_AddViewToPublication 'SiteBoundaryFeatureData';

GO

CREATE VIEW [dbo].[GridSquareFeatureData] WITH SCHEMABINDING AS (
	SELECT
		f.id 
		, gs.gridRef AS label
		, f.geom 
		, gs.geom AS originalGeom
		, p.label AS originalProjection
		, gs.resolutionID 
		, r.label AS resolution
		, gs.parentSquareGridRef 
	FROM [dbo].[Feature] f
	INNER JOIN [dbo].[GridSquare] gs ON f.id = gs.featureID 
	INNER JOIN [dbo].[Projection] p ON p.id = gs.projectionID 
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
		, gsfd.label 
		, gsfd.geom 
		, gsfd.originalGeom 
		, gsfd.originalProjection 
		, gsfd.resolutionID 
	FROM [dbo].[GridSquareFeatureData] gsfd
	UNION ALL
	SELECT 
		sbfd.id
		, sbfd.label 
		, sbfd.geom 
		, sbfd.originalGeom 
		, sbfd.originalProjection 
		, -1 AS resolutionID
	FROM [dbo].[SiteBoundaryFeatureData] sbfd
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
		, t.taxonNavigationGroupKey
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
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngd ON t.taxonNavigationGroupKey = tngd.[key]  
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
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngdp ON t.taxonNavigationGroupKey = tngdp.[key]  
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
	INNER JOIN [dbo].[TaxonNavigationGroupData] tngd ON t.taxonNavigationGroupKey = tngd.[key]  
	WHERE tngd.parentTaxonGroupKey IS NOT NULL 
	GROUP BY dtd.designationID, tngd.[key], tngd.sortOrder, tngd.name, tngd.[description], tngd.parentTaxonGroupKey 
);

GO

--EXEC usp_dev_AddViewToPublicationAsView 'DesignationTaxonNavigationGroupData';

GO

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

--EXEC usp_dev_AddViewToPublicationAsView 'DatasetData';

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