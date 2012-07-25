package uk.org.nbn.nbnv.metadata

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class MetadataParserSuite extends FunSuite with ShouldMatchers {
  test("can parse valid metadata") {

    val parser = new MetadataParser()
    val result = parser.parse(TestData.validMetadataXml)

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
