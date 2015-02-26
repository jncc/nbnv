<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<eml:eml xmlns:eml="eml://ecoinformatics.org/eml-2.1.1" xmlns="eml://ecoinformatics.org/eml-2.1.1" xmlns:dc="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" packageId="NBN//eml-1.xml" scope="system" system="GBIF" xml:lang="en" xsi:schemaLocation="eml://ecoinformatics.org/eml-2.1.1 eml.xsd">
<dataset>
<alternateIdentifier>${dataset.key}</alternateIdentifier>
<title xml:lang="en">${dataset.title}</title>
<creator>
<organizationName>${dataset.organisationName}</organizationName>
<electronicMailAddress>${dataset.organisation.contactEmail}</electronicMailAddress>
</creator>
<metadataProvider>
<individualName>
<givenName>${dataset.organisation.contactName}</givenName>
<surName></surName>
</individualName>
<organizationName>${dataset.organisationName}</organizationName>
<electronicMailAddress>${dataset.organisation.contactEmail}</electronicMailAddress>
<onlineUrl>${dataset.organisation.website}</onlineUrl>
</metadataProvider>
<pubDate>${dataset.dateUploaded?date}</pubDate>
<language>en</language>
<abstract>
<para>${dataset.description}</para>
</abstract>
<intellectualRights>
<para>${dataset.accessConstraints}</para>
</intellectualRights>
<coverage>
<geographicCoverage>
<geographicDescription>${dataset.geographicalCoverage}</geographicDescription>
<boundingCoordinates>
<westBoundingCoordinate>-10</westBoundingCoordinate>
<eastBoundingCoordinate>10</eastBoundingCoordinate>
<northBoundingCoordinate>75</northBoundingCoordinate>
<southBoundingCoordinate>45</southBoundingCoordinate>
</boundingCoordinates>
</geographicCoverage>
<temporalCoverage>
<rangeOfDates>
<beginDate>
<calendarDate>${startDate?date}</calendarDate>
</beginDate>
<endDate>
<calendarDate>${endDate?date}</calendarDate>
</endDate>
</rangeOfDates>
</temporalCoverage>
</coverage>
<purpose>
<para>${dataset.purpose}</para>
</purpose>
<methods>
<methodStep>
<description>
<para>${dataset.captureMethod}</para>
</description>
</methodStep>
<qualityControl>
<description>
<para>${dataset.quality}</para>
</description>
</qualityControl>
</methods>
<additionalInfo>
<para>temporalCoverage: ${dataset.temporalCoverage}</para>
</additionalInfo>
<additionalInfo>
<para>additionalInformation: ${dataset.additionalInformation}</para>
</additionalInfo>
<additionalInfo>
<para>publicPrecision: ${dataset.publicResolution}</para>
</additionalInfo>
<additionalInfo>
<para>recorderAndDeterminerNamesArePublic: ${dataset.publicRecorder?string("true", "false")}</para>
</additionalInfo>
<additionalInfo>
<para>importType: ${isUpsert?string("upsert","append")}</para>
</additionalInfo>
<additionalInfo>
<para>recordAttributesArePublic: ${dataset.publicAttribute?string("true", "false")}</para>
</additionalInfo>
</dataset>
</eml:eml>
