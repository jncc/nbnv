<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<gmd:MD_Metadata xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:srv="http://www.isotc211.org/2005/srv" xmlns:xlink="http://www.w3.org/1999/xlink">
<gmd:fileIdentifier>
<gco:CharacterString>${key}-SERVICE</gco:CharacterString>
</gmd:fileIdentifier>
<gmd:language>
<gmd:LanguageCode codeList="http://www.loc.gov/standards/iso639-2/php/code_list.php" codeListValue="eng">English</gmd:LanguageCode>
</gmd:language>
<gmd:hierarchyLevel>
<gmd:MD_ScopeCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/gmxCodelists.xml#MD_ScopeCode" codeListValue="service">service</gmd:MD_ScopeCode>
</gmd:hierarchyLevel>
<gmd:contact>
<gmd:CI_ResponsibleParty>
<gmd:organisationName>
<gco:CharacterString>National Biodiversity Network</gco:CharacterString>
</gmd:organisationName>
<gmd:contactInfo>
<gmd:CI_Contact>
<gmd:address>
<gmd:CI_Address>
<gmd:electronicMailAddress>
<gco:CharacterString>support@nbn.org.uk</gco:CharacterString>
</gmd:electronicMailAddress>
</gmd:CI_Address>
</gmd:address>
</gmd:CI_Contact>
</gmd:contactInfo>
<gmd:role>
<gmd:CI_RoleCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_ScopeCode" codeListValue="pointOfContact">pointOfContact</gmd:CI_RoleCode>
</gmd:role>
</gmd:CI_ResponsibleParty>
</gmd:contact>
<gmd:dateStamp>
<gco:Date>${date}</gco:Date>
</gmd:dateStamp>
<gmd:metadataStandardName>
<gco:CharacterString>GEMINI2</gco:CharacterString>
</gmd:metadataStandardName>
<gmd:metadataStandardVersion>
<gco:CharacterString>2.1</gco:CharacterString>
</gmd:metadataStandardVersion>
<gmd:referenceSystemInfo>
<gmd:MD_ReferenceSystem>
<gmd:referenceSystemIdentifier>
<gmd:RS_Identifier>
<gmd:code>
<gco:CharacterString>urn:ogc:def:crs:EPSG::27700</gco:CharacterString>
</gmd:code>
<gmd:codeSpace>
<gco:CharacterString>OGP</gco:CharacterString>
</gmd:codeSpace>
</gmd:RS_Identifier>
</gmd:referenceSystemIdentifier>
</gmd:MD_ReferenceSystem>
</gmd:referenceSystemInfo>
<gmd:identificationInfo>
<srv:SV_ServiceIdentification>
<gmd:citation>
<gmd:CI_Citation>
<gmd:title>
<gco:CharacterString>${taxon.name} ${taxon.authority} - Available Species Distribution for the UK</gco:CharacterString>
</gmd:title>
<gmd:date>
<gmd:CI_Date>
<gmd:date>
<gco:Date>${date}</gco:Date>
</gmd:date>
<gmd:dateType>
<gmd:CI_DateTypeCode xmlns="http://www.isotc211.org/2005/gmd" codeList="CI_DateTypeCode" codeListValue="publication">publication</gmd:CI_DateTypeCode>
</gmd:dateType>
</gmd:CI_Date>
</gmd:date>
</gmd:CI_Citation>
</gmd:citation>
<gmd:abstract>
<gco:CharacterString>
This service maps OSGB and OSNI 10km grid squares for ${taxon.name}. These squares were derived from the observations of ${taxon.name} submitted to the National Biodiversity Network. For this dataset, only publically accessible records were used. As not all available data have been made accessible in this way, the data may only represent a partial representation of currently accepted distribution of this species within the UK. Additionally the records have been aggregated to the 10km resolution and are unlikely to be suitable for more local applications.

The following organisations provided data that contributed to this dataset: ${contribs?xml}
</gco:CharacterString>
</gmd:abstract>
<gmd:pointOfContact>
<gmd:CI_ResponsibleParty>
<gmd:organisationName>
<gco:CharacterString>National Biodiversity Network</gco:CharacterString>
</gmd:organisationName>
<gmd:contactInfo>
<gmd:CI_Contact>
<gmd:address>
<gmd:CI_Address>
<gmd:electronicMailAddress>
<gco:CharacterString>support@nbn.org.uk</gco:CharacterString>
</gmd:electronicMailAddress>
</gmd:CI_Address>
</gmd:address>
</gmd:CI_Contact>
</gmd:contactInfo>
<gmd:role>
<gmd:CI_RoleCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_ScopeCode" codeListValue="originator">originator</gmd:CI_RoleCode>
</gmd:role>
</gmd:CI_ResponsibleParty>
</gmd:pointOfContact>
<gmd:pointOfContact>
<gmd:CI_ResponsibleParty>
<gmd:organisationName>
<gco:CharacterString>National Biodiversity Network</gco:CharacterString>
</gmd:organisationName>
<gmd:contactInfo>
<gmd:CI_Contact>
<gmd:address>
<gmd:CI_Address>
<gmd:electronicMailAddress>
<gco:CharacterString>support@nbn.org.uk</gco:CharacterString>
</gmd:electronicMailAddress>
</gmd:CI_Address>
</gmd:address>
</gmd:CI_Contact>
</gmd:contactInfo>
<gmd:role>
<gmd:CI_RoleCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_ScopeCode" codeListValue="pointOfContact">pointOfContact</gmd:CI_RoleCode>
</gmd:role>
</gmd:CI_ResponsibleParty>
</gmd:pointOfContact>
<gmd:resourceMaintenance>
<gmd:MD_MaintenanceInformation>
<gmd:maintenanceAndUpdateFrequency>
<gmd:MD_MaintenanceFrequencyCode xmlns="http://www.isotc211.org/2005/gmd" codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode" codeListValue="irregular">irregular</gmd:MD_MaintenanceFrequencyCode>
</gmd:maintenanceAndUpdateFrequency>
</gmd:MD_MaintenanceInformation>
</gmd:resourceMaintenance>
<gmd:descriptiveKeywords>
<gmd:MD_Keywords>
<gmd:keyword>
<gco:CharacterString>Species distribution</gco:CharacterString>
</gmd:keyword>
<gmd:type>
<gmd:MD_KeywordTypeCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_KeywordTypeCode" codeListValue="theme"/>
</gmd:type>
<gmd:thesaurusName>
<gmd:CI_Citation>
<gmd:title>
<gco:CharacterString>GEMET - INSPIRE themes, version 1.0</gco:CharacterString>
</gmd:title>
<gmd:date>
<gmd:CI_Date>
<gmd:date>
<gco:Date>2008-06-01</gco:Date>
</gmd:date>
<gmd:dateType>
<gmd:CI_DateTypeCode xmlns="http://www.isotc211.org/2005/gmd" codeList="CI_DateTypeCode" codeListValue="publication">publication</gmd:CI_DateTypeCode>
</gmd:dateType>
</gmd:CI_Date>
</gmd:date>
</gmd:CI_Citation>
</gmd:thesaurusName>
</gmd:MD_Keywords>
</gmd:descriptiveKeywords>
<gmd:resourceConstraints>
<gmd:MD_LegalConstraints>
<gmd:useLimitation>
<gco:CharacterString>no conditions apply</gco:CharacterString>
</gmd:useLimitation>
<gmd:accessConstraints>
<gmd:MD_RestrictionCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/gmxCodelists.xml#MD_RestrictionCode" codeListValue="otherRestrictions">otherRestrictions</gmd:MD_RestrictionCode>
</gmd:accessConstraints>
<gmd:otherConstraints>
<gco:CharacterString>Use of this data is subject to the NBN Gateway Terms and Conditions (https://data.nbn.org.uk/Terms). The Data Providers and the NBN Trust bear no responsibility for any further analysis or interpretation of this material.</gco:CharacterString>
</gmd:otherConstraints>
</gmd:MD_LegalConstraints>
</gmd:resourceConstraints>
<srv:serviceType>
    <gco:LocalName>view</gco:LocalName>
</srv:serviceType>
<srv:extent>
<gmd:EX_Extent>
<gmd:geographicElement>
<gmd:EX_GeographicBoundingBox>
<gmd:extentTypeCode>
<gco:Boolean>true</gco:Boolean>
</gmd:extentTypeCode>
<gmd:westBoundLongitude>
<gco:Decimal>-10.698815048871078</gco:Decimal>
</gmd:westBoundLongitude>
<gmd:eastBoundLongitude>
<gco:Decimal>1.7722984184746964</gco:Decimal>
</gmd:eastBoundLongitude>
<gmd:southBoundLatitude>
<gco:Decimal>49.8615998591734</gco:Decimal>
</gmd:southBoundLatitude>
<gmd:northBoundLatitude>
<gco:Decimal>60.90296535852599</gco:Decimal>
</gmd:northBoundLatitude>
</gmd:EX_GeographicBoundingBox>
</gmd:geographicElement>
</gmd:EX_Extent>
</srv:extent>
<srv:coupledResource>
<srv:SV_CoupledResource>
<srv:operationName>
<gco:CharacterString>GetMap</gco:CharacterString>
</srv:operationName>
<srv:identifier>
<gco:CharacterString>NBN:${key}</gco:CharacterString>
</srv:identifier>
</srv:SV_CoupledResource>
</srv:coupledResource>
<srv:coupledResource>
<srv:SV_CoupledResource>
<srv:operationName>
<gco:CharacterString>GetMap</gco:CharacterString>
</srv:operationName>
<srv:identifier>
<gco:CharacterString>https://data.nbn.org.uk/api/taxa/${key}/inspire</gco:CharacterString>
</srv:identifier>
</srv:SV_CoupledResource>
</srv:coupledResource>
<srv:couplingType>
<SV_CouplingType codeListValue="tight" codeList="tight" xmlns="http://www.isotc211.org/2005/srv"/>
</srv:couplingType>
<srv:containsOperations>
<srv:SV_OperationMetadata>
<srv:operationName>
<gco:CharacterString>GetCapabilities</gco:CharacterString>
</srv:operationName>
<srv:DCP>
<DCPList codeListValue="WebService" codeList="WebService" xmlns="http://www.isotc211.org/2005/srv"/>
</srv:DCP>
<srv:connectPoint>
<gmd:CI_OnlineResource>
<gmd:linkage>
<gmd:URL>https://gis.nbn.org.uk/SingleSpecies/${key}/inspire</gmd:URL>
</gmd:linkage>
</gmd:CI_OnlineResource>
</srv:connectPoint>
</srv:SV_OperationMetadata>
</srv:containsOperations>
<srv:containsOperations>
<srv:SV_OperationMetadata>
<srv:operationName>
<gco:CharacterString>GetMap</gco:CharacterString>
</srv:operationName>
<srv:DCP>
<DCPList codeListValue="WebService" codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#DCPList" xmlns="http://www.isotc211.org/2005/srv"/>
</srv:DCP>
<srv:connectPoint>
<gmd:CI_OnlineResource>
<gmd:linkage>
<gmd:URL>https://gis.nbn.org.uk/SingleSpecies/${key}/inspire</gmd:URL>
</gmd:linkage>
</gmd:CI_OnlineResource>
</srv:connectPoint>
</srv:SV_OperationMetadata>
</srv:containsOperations>
<srv:operatesOn xlink:href="NBN:${key}" uuidref="NBN:${key}"/>
<srv:operatesOn xlink:href="https://data.nbn.org.uk/api/taxa/${key}/inspire" uuidref="https://data.nbn.org.uk/api/taxa/${key}/inspire"/>
</srv:SV_ServiceIdentification>
</gmd:identificationInfo>
<gmd:distributionInfo>
<gmd:MD_Distribution>
<gmd:distributionFormat>
<gmd:MD_Format>
<gmd:name>
<gco:CharacterString>png</gco:CharacterString>
</gmd:name>
<gmd:version>
<gco:CharacterString>unknown</gco:CharacterString>
</gmd:version>
</gmd:MD_Format>
</gmd:distributionFormat>
<gmd:distributor>
<gmd:MD_Distributor>
<gmd:distributorContact>
<gmd:CI_ResponsibleParty>
<gmd:organisationName>
<gco:CharacterString>National Biodiversity Network</gco:CharacterString>
</gmd:organisationName>
<gmd:contactInfo>
<gmd:CI_Contact>
<gmd:address>
<gmd:CI_Address>
<gmd:electronicMailAddress>
<gco:CharacterString>support@nbn.org.uk</gco:CharacterString>
</gmd:electronicMailAddress>
</gmd:CI_Address>
</gmd:address>
</gmd:CI_Contact>
</gmd:contactInfo>
<gmd:role>
<gmd:CI_RoleCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_ScopeCode" codeListValue="distributor">distributor</gmd:CI_RoleCode>
</gmd:role>
</gmd:CI_ResponsibleParty>
</gmd:distributorContact>
</gmd:MD_Distributor>
</gmd:distributor>
<gmd:transferOptions>
<gmd:MD_DigitalTransferOptions>
<gmd:onLine>
<gmd:CI_OnlineResource>
<gmd:linkage>
<gmd:URL>https://gis.nbn.org.uk/SingleSpecies/${key}/inspire?request=getCapabilities&amp;service=WMS</gmd:URL>
</gmd:linkage>
<gmd:name>
<gco:CharacterString>WMS Service</gco:CharacterString>
</gmd:name>
<gmd:description>
<gco:CharacterString>GetCapabilities for this service</gco:CharacterString>
</gmd:description>
</gmd:CI_OnlineResource>
</gmd:onLine>
</gmd:MD_DigitalTransferOptions>
</gmd:transferOptions>
</gmd:MD_Distribution>
</gmd:distributionInfo>
<gmd:dataQualityInfo>
<gmd:DQ_DataQuality>
<gmd:scope>
<gmd:DQ_Scope>
<gmd:level>
<MD_ScopeCode xmlns="http://www.isotc211.org/2005/gmd" codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_ScopeCode" codeListValue="service"/>
</gmd:level>
<gmd:levelDescription>
<gmd:MD_ScopeDescription>
<gmd:other>
<gco:CharacterString>service</gco:CharacterString>
</gmd:other>
</gmd:MD_ScopeDescription>
</gmd:levelDescription>
</gmd:DQ_Scope>
</gmd:scope>
<gmd:lineage>
<gmd:LI_Lineage>
<gmd:statement>
<gco:CharacterString>This dataset was derived from the data holdings of the NBN Gateway (https://data.nbn.org.uk) and represents an aggregation of multiple sources. More recent and more detailed data may be available from the contributing data providers as only publically accessible data have been used. More detailed access can be negotiated through the NBN Gateway. The underlying data are of varying quality and have been subjected to different data quality processes and not all data have been verified. Also, historical data have been included so the modern distribution may be narrower than indicated. As a result care should be exercised when interpreting these data.</gco:CharacterString>
</gmd:statement>
</gmd:LI_Lineage>
</gmd:lineage>
</gmd:DQ_DataQuality>
</gmd:dataQualityInfo>
</gmd:MD_Metadata>
