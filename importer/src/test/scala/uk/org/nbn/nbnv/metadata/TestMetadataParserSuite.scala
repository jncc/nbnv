package uk.org.nbn.nbnv.metadata

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestMetadataParserSuite extends FunSuite with ShouldMatchers {
  test("can parse valid metadata") {
    val validXml = <eml xmlns="eml://ecoinformatics.org/eml-2.1.1" xmlns:dc="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" packageId="NBN/GA000159/eml-1.xml" scope="system" system="GBIF" xml:lang="en" xsi:schemaLocation="eml://ecoinformatics.org/eml-2.1.1 eml.xsd">
      <dataset>
        <alternateIdentifier>Test Identifier</alternateIdentifier>
        <title xml:lang="en">Test Title</title>
        <abstract>
          <para>Test Description</para>
        </abstract>
        <intellectualRights>
          <para>Access Constraints: Test Access constraints Use Constraints: Test Use Constraints</para>
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
      </dataset>
    </eml>

    val parser = new MetadataParser()
    val result = parser.parse(validXml)

    result.accessConstraints should be("Test Access constraints")
    result.datasetKey should be("Test Identifier")
    result.datasetTitle should be("Test Title")
    result.description should be("Test Description")
    result.geographicCoverage should be("Test geographic coverage")
    result.method should be("Test Method")
    result.quality should be("Test Quality")
    result.purpose should be("Test purpose")
    result.useConstraints should be("Test Use Constraints")
  }
}
