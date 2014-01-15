package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.Options

class DataFileParserSuite extends BaseFunSuite {
  def fixture = new {

    val log = mock[Logger]
    val options = mock[Options]

    val dataFilePath = "some/Path"

    val metadata = mock[ArchiveMetadata]
    when(metadata.skipHeaderLines).thenReturn(Some(1))

    val rawData = mock[List[String]]
    val iterableWithIndex = mock[Iterable[(List[String], Int)]]

    val iterable = mock[Iterable[List[String]]]
    when(iterable.zipWithIndex).thenReturn(iterableWithIndex)

    val csvReader = mock[CsvReader]
    when(csvReader.drop(1)).thenReturn(iterable)

    val csvReaderFactory = mock[CsvReaderFactory]
    when(csvReaderFactory.openCsvReader(dataFilePath)).thenReturn(csvReader)

    val recordFactory = mock[NbnRecordFactory]

    val dataFileParser = new DataFileParser(options, recordFactory, log, csvReaderFactory)
  }

  test("should initialise the csv reader correctly") {
    val f = fixture

    f.dataFileParser.open(f.dataFilePath, f.metadata)

    verify(f.csvReaderFactory).openCsvReader(f.dataFilePath)
  }

  test("should thow excpetion if dataFileParser has not been opened") {
    val f = fixture

    intercept[IllegalStateException] {
      f.dataFileParser.records
    }
  }
}
