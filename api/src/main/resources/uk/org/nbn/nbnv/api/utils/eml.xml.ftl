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
<givenName>????</givenName>
<surName>????</surName>
</individualName>
<organizationName>${dataset.organisationName}</organizationName>
<electronicMailAddress>${dataset.organisation.contactEmail}</electronicMailAddress>
<onlineUrl>dataset.organisation.website</onlineUrl>
</metadataProvider>
<pubDate>2014-12-05</pubDate>
<language>en</language>
<abstract>
<para>Sightings of 10 common birds species within three national nature reserves, Monks Wood, Woodwalton Fen and Holme Fen, occuring in or adjacent to TL28 10km grid square.</para>
</abstract>
<intellectualRights>
<para>accessConstraints: In future, to allow demonstration of the access controls on the NBN Gateway, a range of access constraints will be applied to this dataset. For demonstration the Treecreeper records are flagged as sensitive. useConstraints: None</para>
</intellectualRights>
<coverage>
<geographicCoverage>
<geographicDescription>All bird sightings are within the three national nature reserves, Monks Wood, Woodwalton Fen and Holme Fen. All three occur within the 10km grid square TL28. Both Holme Fen and Monks Wood also occur in adjacent 10km grid squares</geographicDescription>
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
<calendarDate>2008-02-19</calendarDate>
</beginDate>
<endDate>
<calendarDate>2008-04-13</calendarDate>
</endDate>
</rangeOfDates>
</temporalCoverage>
</coverage>
<purpose>
<para>Purpose of dataset is to demonstrate the access controls of the NBN Gateway, using bird sighting records gathered during walks through each of the three national nature reserves.</para>
</purpose>
<methods>
<methodStep>
<description>
<para>All bird sightings were recorded, using their common name, at 100m (six-figure grid reference) resolution, estimated from Ordnance Survey 1:50 000 map.</para>
</description>
</methodStep>
<qualityControl>
<description>
<para>The majority of bird sightings were recorded at 100m (six-figure grid reference) resolution, estimated from ordnance survey 1:50 000 map. For demonstration purposes the Moorhen records were recorded at 10m (eight-figure grid reference) and Mute Swan records at 1km (four-figure grid reference).</para>
</description>
</qualityControl>
</methods>
<additionalInfo>
<para>temporalCoverage: All bird sightings were made during 2008, with dates given as the day of the sighting.</para>
</additionalInfo>
<additionalInfo>
<para>additionalInformation: </para>
</additionalInfo>
<additionalInfo>
<para>publicPrecision: 10000</para>
</additionalInfo>
<additionalInfo>
<para>recorderAndDeterminerNamesArePublic: false</para>
</additionalInfo>
<additionalInfo>
<para>importType: upsert</para>
</additionalInfo>
<additionalInfo>
<para>recordAttributesArePublic: false</para>
</additionalInfo>
</dataset>
</eml:eml>
