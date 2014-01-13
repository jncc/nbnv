package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import java.io.File

class CsvReaderSuite extends BaseFunSuite with ResourceLoader {

  def fixture() = new {
    val dataFilePath = resource("/archives/valid/data.tab")
    val csvReader = new CSVReader(new File(dataFilePath.getFile))
  }

  test("should read records") {
    val f = fixture()
    var lastIndex : Int = 0
    f.csvReader.zipWithIndex.map{ case (record, index) => {
      lastIndex = index
    }}

    lastIndex should be (7) // 7 reacords + 1 header line
  }

  test("should read 22 fields in each row") {
    val f = fixture()
    f.csvReader.drop(1).zipWithIndex.map{case (record, index) =>
      record.length should be (22)
    }
  }
}
