--CREATE SCHEMA PUBLIC AUTHORIZATION DBA;
--CREATE USER SA PASSWORD "";
--GRANT DBA TO SA;
SET WRITE_DELAY 10;
SET SCHEMA PUBLIC;
CREATE MEMORY TABLE OrganisationData (organisationID integer NOT NULL, name varchar(200) NOT NULL);
INSERT INTO OrganisationData VALUES (1, 'Test Organisation');
commit;
shutdown;