package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite


class ArchiveMetadataParserSuite extends BaseFunSuite {

  test("should get correct column id's from test data") {
    val xml = TestData.validMetadataXml

    val amp = new ArchiveMetadataParser

    val result = amp.getMetadata(xml)

    result.fieldSeparator should be ("\\t")

    result.skipHeaderLines should be (Some(1))

    result.date should be (Some(22))
    result.startDate should be (Some(3))
    result.gridReferenceType should be (Some(10))
    result.sensitiveOccurrence should be (Some(7))
    result.siteName should be (Some(9))
    result.key should be (Some(0))
    result.gridReferencePrecision should be (Some(12))
    result.recorder should be (Some(13))
    result.siteKey should be (Some(8))
    result.attributes should be (Some(18))
    result.srs should be (Some(19))
    result.taxonVersionKey should be (Some(6))
    result.north should be (Some(21))
    result.dateType should be (Some(5))
    result.endDate should be (Some(4))
    result.gridReference should be (Some(11))
    result.absence should be (Some(23))
    result.surveyKey should be (Some(1))
    result.east should be (Some(20))
    result.sampleKey should be (Some(2))
  }

}
