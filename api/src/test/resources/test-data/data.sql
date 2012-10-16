--Populate some designation categories
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (19, N'International', N'International legislative lists and conventions (excludes global red lists)', 1);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (20, N'Nat Legislation', N'National legislative lists eg Wildlife and Countryside Act schedules', 5);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (21, N'Other rare/scarce', N'Other lists of rare and scarce species excluding those based on IUCN criteria for red listings', 4);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (22, N'Red Data List', N'Red lists based on the IUCN criteria', 2);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (23, N'UK BAP', N'UK BAP priority species lists', 3); 
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (24, N'Organisation Defined Lists', N'Miscelleneous defined lists', 6);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (100, N'Embedded Record', N'Embedded Record test', 7);

-- ----------------
--Create a single user "tester" with an id of 44
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    44, 'Test', 'User', 'tester2', 'test@user.com', NULL, NULL
);

--Create an authentication user table with a single userid "43" and a password of "password
INSERT INTO UserTokenAuthenticationData (
    id, username_sha1, password_sha1, password_md5_sha1 
) VALUES (44, x'5E86E8CDC7188D53916FCD1D7294CEE4611C7C49', x'5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8', x'3E3295D135520D1F6D1FDCB183AE356C48ECB8FD');

-- ----------------
--Create a single user "tester" with an id of 43
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    43, 'Test', 'User', 'tester', 'test@user.com', NULL, NULL
);
   
--Create an authentication user table with a single userid "43" and a password of "password
INSERT INTO UserTokenAuthenticationData (
    id, username_sha1, password_sha1, password_md5_sha1 
) VALUES (43, x'AB4D8D2A5F480A137067DA17100271CD176607A1', x'5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8', x'3E3295D135520D1F6D1FDCB183AE356C48ECB8FD');

-- ---------------
--Create a single admin "admintester" with an id of 42
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    42, 'Test', 'Admin', 'admintester', 'testadmin@user.com', NULL, NULL
);

INSERT INTO UserRoleSystemAdministratorData (userKey) VALUES (42);

-- ---------------
--Create a test organisation "testorg" with id 1
INSERT INTO OrganisationData (
    organisationID, name
) VALUES (
    1, 'testorg'
);

--Create a single organisation admin "orgadmintester" with an id of 41
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    41, 'Test', 'OrgAdmin', 'orgadmintester', 'testorgadmin@user.com', NULL, NULL
);

--Associate orgadmintester as admin of testorg
INSERT INTO OrganisationMembershipData (
    userKey, organisationID, role
) VALUES (
    41, 1, 'administrator'
);

--Create a single organisation member "orgmembertester" with an id of 40
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    40, 'Test', 'OrgMember', 'orgmembertester', 'testorgmember@user.com', NULL, NULL
);

--Associate orgmembertester as member of testorg
INSERT INTO OrganisationMembershipData (
    userKey, organisationID, role
) VALUES (
    40, 1, 'member'
);

-- ------------------
--Create a test organisation "testdataorg" with id 2
INSERT INTO OrganisationData (
    organisationID, name
) VALUES (
    2, 'testdataorg'
);

--Create a test dataset as 'DATASET1'
INSERT INTO DatasetData (
	key, name, typeName, organisationID, dateUploaded, conditionsAccepted, metadataLastEdited, updateFrequency
) VALUES (
    'DATASET1', 'Test Dataset', 'Taxon', 2, '2012-05-09 11:11:30', 1, '2012-05-09 11:11:30', 'unknown'
);

--Create a single dataset admin "datasetadmintester" with an id of 39
INSERT INTO UserData (
    id, forename, surname, username, email, registrationDate, phone
) VALUES (
    39, 'Test', 'DatasetAdmin', 'datasetadmintester', 'testdatasetadmin@user.com', NULL, NULL
);

--Make "datasetadmintester" the test datasets admin
INSERT INTO DatasetAdministrator (
    userKey, datasetKey
) VALUES (
    39, 'DATASET1'
);