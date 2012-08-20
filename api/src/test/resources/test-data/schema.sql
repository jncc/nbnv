CREATE TABLE UserData(
	id int NOT NULL PRIMARY KEY IDENTITY,
	forename varchar(25) NOT NULL,
	surname varchar(25) NOT NULL,
	username varchar(40) NOT NULL,
	email varchar(70) NOT NULL,
	registrationDate datetime NULL,
	phone varchar(15) NULL);

CREATE TABLE UserAccess(
	userKey int NOT NULL,
	observationID int NOT NULL,
        PRIMARY KEY (userKey, observationID));

CREATE TABLE TaxonObservationData(
	observationID int NOT NULL,
	fullVersion bit NOT NULL,
	datasetKey char(8) NOT NULL,
	surveyKey varchar(100) NOT NULL,
	sampleKey varchar(100) NOT NULL,
	observationKey varchar(100) NOT NULL,
	siteKey varchar(100) NULL,
	siteName varchar(200) NULL,
	featureID int NOT NULL,
	gridRef varchar(12) NULL,
	polygonKey varchar(100) NULL,
	projection varchar(50) NOT NULL,
	taxonVersionKey char(16) NOT NULL,
	pTaxonVersionKey char(16) NOT NULL,
	pTaxonName varchar(85) NOT NULL,
	pTaxonAuthority varchar(80) NULL,
	startDate datetime(7) NOT NULL,
	endDate datetime(7) NOT NULL,
	dateType varchar(2) NOT NULL,
	recorder varchar(140) NULL,
	determiner varchar(140) NULL,
	sensitive bit NOT NULL,
	absence bit NOT NULL,
        PRIMARY KEY (observationID, fullVersion));

CREATE TABLE TaxonGroupData(
	taxonGroupKey char(16) NOT NULL,
	sortOrder int NULL,
	outputFlag bit NOT NULL,
	taxonGroupName varchar(50) NULL,
	descriptor varchar(65) NULL,
	parent char(16) NULL,
	taxonCount int NULL,
        PRIMARY KEY (taxonGroupKey));

CREATE TABLE TaxonData(
	taxonVersionKey char(16) NOT NULL,
	prefnameTaxonVersionKey char(16) NOT NULL,
	name varchar(8000) NULL,
	authority varchar(8000) NULL,
	lang char(2) NULL,
	outputGroupKey char(16) NULL,
	navigationGroupKey char(16) NULL,
        PRIMARY KEY (taxonVersionKey));

CREATE TABLE SiteOverlaps(
	siteFeatureID int NOT NULL,
	gridFeatureID int NOT NULL,
        PRIMARY KEY (siteFeatureID, gridFeatureID));

CREATE TABLE SiteContains(
	siteFeatureID int NOT NULL,
	gridFeatureID int NOT NULL,
        PRIMARY KEY (siteFeatureID, gridFeatureID));

CREATE TABLE SiteBoundaryDatasetData(
	datasetKey char(8) NOT NULL,
	name varchar(200) NOT NULL,
	description varchar(8000) NULL,
	dateUploaded datetime NOT NULL,
	provider varchar(200) NULL,
	providerID int NOT NULL,
	geoLayerName varchar(100) NOT NULL,
	nameField varchar(100) NOT NULL,
	siteBoundaryCategory int NOT NULL,
	siteCategoryName varchar(8000) NOT NULL,
        PRIMARY KEY (datasetKey));

CREATE TABLE SiteBoundaryData(
	featureID int NOT NULL,
	name varchar(200) NOT NULL,
	providerKey varchar(100) NOT NULL,
	siteBoundaryDatasetKey char(8) NOT NULL,
	siteBoundaryCategoryID int NOT NULL,
        PRIMARY KEY (featureID));

CREATE TABLE SiteBoundaryCategory(
	siteBoundaryCategoryID int NOT NULL,
	name varchar(8000) NOT NULL,
        PRIMARY KEY (siteBoundaryCategoryID));

CREATE TABLE OrganisationData(
	organisationID int NOT NULL,
	name varchar(200) NOT NULL);

CREATE TABLE GridTree(
	featureID int NOT NULL,
	parentFeatureID int NOT NULL);

CREATE TABLE DesignationTaxonNavigationGroupData(
	designationID int NOT NULL,
	taxonGroupKey char(16) NOT NULL,
	taxonGroupName varchar(50) NULL,
	descriptor varchar(65) NULL,
	sortOrder int NULL,
	parent char(16) NULL,
	numSpecies int NULL);

CREATE TABLE DesignationTaxonData(
	designationID int NOT NULL,
	code varchar(100) NOT NULL,
	pTaxonVersionKey char(16) NOT NULL,
	PRIMARY KEY (designationID, pTaxonVersionKey));

CREATE TABLE DesignationData(
	designationID int NOT NULL,
	name varchar(8000) NOT NULL,
	label varchar(50) NOT NULL,
	code varchar(100) NOT NULL,
	designationCategoryID int NOT NULL,
	description varchar(8000) NULL);

CREATE TABLE DesignationCategoryData(
	designationCategoryID int NOT NULL,
	label varchar(50) NOT NULL,
	description varchar(8000) NULL,
	sortOrder int NOT NULL);

CREATE TABLE UserTokenAuthenticationData(
	id int NOT NULL,
	username_sha1 varbinary(8000) NULL,
	password_sha1 varbinary(8000) NULL,
	password_md5_sha1 varbinary(8000) NULL,
	FOREIGN KEY (id) REFERENCES UserData(id));
