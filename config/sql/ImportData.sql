USE [NBNCore] 
GO

/*
 *
 * User
 *
 */

INSERT INTO [User] (
	username
	, password_sha1
	, password_md5_sha1
	, userTypeID 
	, forename 
	, surname 
	, phone
	, email 
	, active 
	, activationKey 
	, invalidEmail
	, allowEmailAlerts
	, subscribedToAdminMails
	, subscribedToNBNMarketting
	, bannedFromValidation
	, englishNameOrder
	, registrationDate
	, lastLoggedIn
) SELECT 
	u.username
	, HASHBYTES('SHA1', u.[password])
	, HASHBYTES('SHA1', HASHBYTES('MD5', u.[password]))
	, 2
	, u.forename 
	, u.surname 
	, u.phone 
	, u.email 
	, u.active 
	, 'IMPORTED'
	, u.invalidEmail 
	, u.allowEmailAlerts 
	, u.subscribedToAdminMails 
	, u.subcribedToNBNMarketting 
	, u.bannedFromValidation 
	, ISNULL(u.englishNameOrder, 0)
	, u.registrationDate 
	, u.lastLoggedIn 
FROM [NBNGatewayOrig].dbo.NBNUser u
INNER JOIN (
	SELECT a.email, COUNT(*) AS accts
	FROM [NBNGatewayOrig].dbo.NBNUser a
	WHERE a.active = 1
	GROUP BY a.email
) b ON b.email = u.email
WHERE u.active = 1
AND b.accts = 1
AND u.userKey > 0


GO

UPDATE [User] SET userTypeID = 3 WHERE email IN (
	SELECT u.email COLLATE Latin1_General_CI_AS
	FROM [NBNGatewayOrig].dbo.NBNAdminUser au
	INNER JOIN [NBNGatewayOrig].dbo.NBNUser u ON u.userKey = au.userKey 
	AND au.userKey IN (3, 1, 18184, 13765)
)

GO


/*
 *
 * Organisation
 *
 */

INSERT INTO [Organisation] (
	name 
	, abbreviation 
	, summary 
	, [address] 
	, postcode 
	, phone 
	, website 
	, contactName 
	, contactEmail 
	, allowPublicRegistration 
) SELECT 
	o.organisationName 
	, CASE WHEN o.abbreviation = '' THEN NULL ELSE o.abbreviation END
	, o.summary 
	, o.[address] 
	, o.postcode 
	, o.phone 
	, o.website 
	, o.contactName 
	, o.contactEmail 
	, ISNULL(o.allowPublicRegistration, 0)
FROM [NBNGatewayOrig].dbo.Organisation o

GO

INSERT INTO [UserOrganisationMembership] (
	userID 
	, organisationID 
	, organisationRoleID 
) SELECT
	cu.id
	, co.id
	, c.userRole
FROM (
	SELECT
		u.email COLLATE Latin1_General_CI_AS AS userAcct
		, o.organisationName COLLATE Latin1_General_CI_AS AS org
		, a.userRole 
	FROM (
		SELECT 
			uor.userKey
			, uor.organisationKey 
			, MAX(uor.userRole) AS userRole
		FROM [NBNGatewayOrig].dbo.UserOrganisationRole uor
		WHERE uor.userRole > 0
		GROUP BY uor.userKey, uor.organisationKey 
	) a
	INNER JOIN [NBNGatewayOrig].dbo.NBNUser u ON u.userKey = a.userKey 
	INNER JOIN [NBNGatewayOrig].dbo.Organisation o ON o.organisationKey = a.organisationKey 
) c
INNER JOIN [User] cu ON cu.email = c.userAcct 
INNER JOIN [Organisation] co ON co.name = c.org 

GO

WITH tree AS (
	SELECT o.ORGANISM_KEY, o.PARENT_KEY 
	FROM [SpeciesDictionary].dbo.ORGANISM o 
	WHERE o.PARENT_KEY IS NULL AND o.REDUNDANT_FLAG IS NULL
	UNION ALL
	SELECT o.ORGANISM_KEY, o.PARENT_KEY
	FROM [SpeciesDictionary].dbo.ORGANISM o 
	JOIN tree t ON o.PARENT_KEY = t.ORGANISM_KEY 
	WHERE o.REDUNDANT_FLAG IS NULL
)
INSERT INTO [Organism] ([key], [parentOrganismKey])
SELECT * FROM tree

GO

INSERT INTO [TaxonRank] (id, [rank], [level])
SELECT 
	ROW_NUMBER() OVER (ORDER BY tr.TAXON_RANK_KEY ASC)
	, tr.LONG_NAME 
	, tr.SEQUENCE 
FROM [SpeciesDictionary].dbo.TAXON_RANK tr

GO

INSERT INTO [TaxonGroup] 
SELECT 
	tgn.TAXON_GROUP_KEY 
	, tgn.TAXON_GROUP_NAME
	, LEFT(ISNULL(tgn.INPUT_LEVEL2_DESCRIPTOR, tgn.INPUT_LEVEL1_DESCRIPTOR), 65)
	, tgn.INPUT_LEVEL1_SORT_CODE 
	, tgn.OUTPUT_FLAG 
	, tgn.PARENT 
FROM [SpeciesDictionary].dbo.TAXON_GROUP_NAME tgn

GO

INSERT INTO [Taxon] 
SELECT 
	tv.TAXON_VERSION_KEY 
	, n.RECOMMENDED_TAXON_VERSION_KEY 
	, o.ORGANISM_KEY 
	, LEFT(t.ITEM_NAME + CASE WHEN tv.ATTRIBUTE IS NULL THEN '' ELSE ' ' + tv.ATTRIBUTE END, 85)
	, t.AUTHORITY 
	, t.[LANGUAGE]
	, null
	, t.ABBREVIATION 
	, ctr.id 
	, ISNULL(n.TAXON_VERSION_STATUS, 'U')
	, ISNULL(n.TAXON_VERSION_FORM, 'U')
	, tv.OUTPUT_GROUP_KEY 
FROM [SpeciesDictionary].dbo.TAXON t
INNER JOIN [SpeciesDictionary].dbo.TAXON_VERSION tv ON tv.TAXON_KEY = t.TAXON_KEY 
INNER JOIN [SpeciesDictionary].dbo.TAXON_RANK tr ON tr.TAXON_RANK_KEY = tv.TAXON_RANK_KEY 
INNER JOIN [NBNCore].dbo.TaxonRank ctr ON ctr.[rank] = tr.LONG_NAME AND ctr.[level] = tr.SEQUENCE 
INNER JOIN [SpeciesDictionary].dbo.NAMESERVER n ON n.INPUT_TAXON_VERSION_KEY = tv.TAXON_VERSION_KEY 
INNER JOIN [SpeciesDictionary].dbo.ORGANISM o ON o.TAXON_VERSION_KEY = n.RECOMMENDED_TAXON_VERSION_KEY AND o.REDUNDANT_FLAG IS NULL

GO

INSERT INTO [TaxonAggregate]
SELECT 
	a.AGGREGATE_TVK
	, a.COMPONENT_TVK 
FROM [SpeciesDictionary].[dbo].[AGGREGATE] a
INNER JOIN [NBNCore].[dbo].[Taxon] t ON t.taxonVersionKey = a.AGGREGATE_TVK 
INNER JOIN [NBNCore].[dbo].[Taxon] tb ON tb.taxonVersionKey = a.COMPONENT_TVK 

GO

INSERT INTO [DesignationCategory] (label, [description], sortOrder)
SELECT 
	tdtk.KIND
	, tdtk.ITEM_NAME
	, ROW_NUMBER() OVER (ORDER BY tdtk.TAXON_DESIGNATION_TYPE_KIND_KEY)
FROM [SpeciesDictionary].[dbo].[TAXON_DESIGNATION_TYPE_KIND] tdtk

GO

INSERT INTO [Designation] (name, label, code, designationCategoryID, [description])
SELECT DISTINCT
	tdt.LONG_NAME 
	, tdt.SHORT_NAME 
	, UPPER(tdt.SHORT_NAME)
	, dc.id 
	, tdt.[DESCRIPTION]
FROM [SpeciesDictionary].[dbo].[TAXON_DESIGNATION] td
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_DESIGNATION_TYPE] tdt ON tdt.TAXON_DESIGNATION_TYPE_KEY = td.TAXON_DESIGNATION_TYPE_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_ITEM] tli ON tli.TAXON_LIST_ITEM_KEY = td.TAXON_LIST_ITEM_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_VERSION] tlv ON tlv.TAXON_LIST_VERSION_KEY = tli.TAXON_LIST_VERSION_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST] tl ON tl.TAXON_LIST_KEY = tlv.TAXON_LIST_KEY 
INNER JOIN [NBNCore].[dbo].[DesignationCategory] dc ON dc.label = tdt.KIND 
WHERE tl.TAXON_LIST_KEY = 'NHMSYS0020424779'

GO

INSERT INTO [TaxonDesignation]  
SELECT
	t.pTaxonVersionKey 
	, d.id 
	, td.DATE_FROM 
	, td.DATE_TO 
	, STUFF((
			SELECT
				', ' + a.DETAIL
			FROM (
				SELECT 
					tli.TAXON_VERSION_KEY 
					, td.TAXON_DESIGNATION_TYPE_KEY 
					, td.DETAIL 
				FROM [SpeciesDictionary].[dbo].[TAXON_DESIGNATION] td
				INNER JOIN [SpeciesDictionary].[dbo].[TAXON_DESIGNATION_TYPE] tdt ON tdt.TAXON_DESIGNATION_TYPE_KEY = td.TAXON_DESIGNATION_TYPE_KEY 
				INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_ITEM] tli ON tli.TAXON_LIST_ITEM_KEY = td.TAXON_LIST_ITEM_KEY 
				INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_VERSION] tlv ON tlv.TAXON_LIST_VERSION_KEY = tli.TAXON_LIST_VERSION_KEY 
				INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST] tl ON tl.TAXON_LIST_KEY = tlv.TAXON_LIST_KEY 
				INNER JOIN [NBNCore].[dbo].[Designation] d ON d.label = tdt.SHORT_NAME 
				INNER JOIN [NBNCore].[dbo].[Taxon] t ON t.taxonVersionKey = tli.TAXON_VERSION_KEY 
				WHERE tl.TAXON_LIST_KEY = 'NHMSYS0020424779'
			) a
			WHERE a.TAXON_VERSION_KEY = tli.TAXON_VERSION_KEY AND a.TAXON_DESIGNATION_TYPE_KEY = tdt.TAXON_DESIGNATION_TYPE_KEY 
			FOR XML PATH('')
		), 1, 2, '') AS d
	, td.STATUS_CONSTRAINT 
FROM [SpeciesDictionary].[dbo].[TAXON_DESIGNATION] td
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_DESIGNATION_TYPE] tdt ON tdt.TAXON_DESIGNATION_TYPE_KEY = td.TAXON_DESIGNATION_TYPE_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_ITEM] tli ON tli.TAXON_LIST_ITEM_KEY = td.TAXON_LIST_ITEM_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST_VERSION] tlv ON tlv.TAXON_LIST_VERSION_KEY = tli.TAXON_LIST_VERSION_KEY 
INNER JOIN [SpeciesDictionary].[dbo].[TAXON_LIST] tl ON tl.TAXON_LIST_KEY = tlv.TAXON_LIST_KEY 
INNER JOIN [NBNCore].[dbo].[Designation] d ON d.label = tdt.SHORT_NAME 
INNER JOIN [NBNCore].[dbo].[Taxon] t ON t.taxonVersionKey = tli.TAXON_VERSION_KEY 
WHERE tl.TAXON_LIST_KEY = 'NHMSYS0020424779'

GO

INSERT INTO [NBNCore].[dbo].[GridExtents]
SELECT 
	nge.ogr_fid 
	, nge.ogr_geometry 
	, nge.projection 
	, nge.[priority]
FROM [NBNSpatial].[dbo].[nationalgridextents] nge

GO

INSERT INTO [dbo].[SiteBoundaryType] (id, siteTypeName)
SELECT
	asla.asl1Key 
	, asla.siteTypeName 
FROM [NBNGatewayOrig].[dbo].[AdminSiteLevel1] asla

GO

INSERT INTO [dbo].[SiteBoundaryType] (id, parentID, siteTypeName, siteTypeCode, xmlEnumValue)
SELECT
	asla.asl2Key + 23
	, asla.asl1Key 
	, asla.siteTypeName 
	, asla.siteTypeCode 
	, asla.xmlEnumValue 
FROM [NBNGatewayOrig].[dbo].[AdminSiteLevel2] asla

GO