package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite


class ArchiveMetadataParserSuite extends BaseFunSuite {

  test("should get a field id for date") {
    val xml = TestData.validMetadataXml

    val amp = new ArchiveMetadataParser

    val result = amp.getMetadata(xml)

    result.date should be (Some(22))
    result.startDate should be (None)
  }

}
