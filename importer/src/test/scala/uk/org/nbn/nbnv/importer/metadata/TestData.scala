package uk.org.nbn.nbnv.importer.metadata

object TestData {
  val validMetadataXml = <eml xmlns="eml://ecoinformatics.org/eml-2.1.1" xmlns:dc="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" packageId="NBN/GA000159/eml-1.xml" scope="system" system="GBIF" xml:lang="en" xsi:schemaLocation="eml://ecoinformatics.org/eml-2.1.1 eml.xsd">
    <dataset>
      <alternateIdentifier>Test Identifier</alternateIdentifier>
      <title xml:lang="en">Test Title</title>
      <creator>
        <organizationName>Test organisation</organizationName>
        <electronicMailAddress/>
      </creator>
      <metadataProvider>
        <individualName>
          <givenName>Test</givenName>
          <surName>User</surName>
        </individualName>
        <organizationName>Test metatdata orga</organizationName>
        <electronicMailAddress>test.user@example.com</electronicMailAddress>
        <onlineUrl>http://example.com/</onlineUrl>
      </metadataProvider>
      <abstract>
        <para>Test Description</para>
      </abstract>
      <intellectualRights>
        <para>accessConstraints: Test Access constraints useConstraints: Test Use Constraints</para>
      </intellectualRights>
      <coverage>
        <geographicCoverage>
          <geographicDescription>Test geographic coverage</geographicDescription>
        </geographicCoverage>
      </coverage>
      <purpose>
        <para>Test purpose</para>
      </purpose>
      <methods>
        <methodStep>
          <description>
            <para>Test Method</para>
          </description>
        </methodStep>
        <qualityControl>
          <description>
            <para>Test Quality</para>
          </description>
        </qualityControl>
      </methods>
      <additionalInfo>
        <para>temporalCoverage: Test temporal coverage</para>
      </additionalInfo>
      <additionalInfo>
        <para>additionalInformation: Test additional info</para>
      </additionalInfo>
    </dataset>
  </eml>
}
