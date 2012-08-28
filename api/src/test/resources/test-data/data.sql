--Populate some designation categories
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (19, N'International', N'International legislative lists and conventions (excludes global red lists)', 1);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (20, N'Nat Legislation', N'National legislative lists eg Wildlife and Countryside Act schedules', 5);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (21, N'Other rare/scarce', N'Other lists of rare and scarce species excluding those based on IUCN criteria for red listings', 4);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (22, N'Red Data List', N'Red lists based on the IUCN criteria', 2);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (23, N'UK BAP', N'UK BAP priority species lists', 3); 
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (24, N'Organisation Defined Lists', N'Miscelleneous defined lists', 6);
INSERT INTO DesignationCategoryData ( designationCategoryID , label , description , sortOrder ) VALUES (100, N'Embedded Record', N'Embedded Record test', 7);

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