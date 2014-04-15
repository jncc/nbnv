package uk.org.nbn.nbnv.importer.metadata

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class MetadataParserSuite extends BaseFunSuite {

  test("can parse valid metadata") {

    val parser = new MetadataParser()
    val result = parser.parse(TestData.validMetadataXml)

    result.accessConstraints should be("Test Access constraints")
    result.datasetKey should be("Test Identifier")
    result.datasetTitle should be("Test Title")
    result.description should be("Test Description")
    result.geographicCoverage should be("Test geographic coverage")
    result.dataCaptureMethod should be("Test Method")
    result.dataQuality should be("Test Quality")
    result.purpose should be("Test purpose")
    result.useConstraints should be("Test Use Constraints")
    result.temporalCoverage should be ("Test temporal coverage")
    result.additionalInformation should be ("Test additional info")
    result.administratorEmail should be ("test.user@example.com")
    result.recorderAndDeterminerArePublic should be (false)
    result.attributesArePublic should be (false)
    result.importType should be (Some(Mode.upsert))
  }
}
