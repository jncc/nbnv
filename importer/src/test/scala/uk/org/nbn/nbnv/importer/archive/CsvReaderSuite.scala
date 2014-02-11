package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import java.io.File

//This test suit is dependant on the data in the test data file
class CsvReaderSuite extends BaseFunSuite with ResourceLoader {

  def fixture() = new {
    val dataFilePath = resource("/archives/valid/data.tab")
    val csvReader = new CsvReader(new File(dataFilePath.getFile))
  }

  test("should read records") {
    val f = fixture()
    var lastIndex : Int = 0
    f.csvReader.zipWithIndex.map{ case (record, index) => {
      lastIndex = index
    }}

    lastIndex should be (7) // 7 reacords + 1 header line
  }

//  test("should read 22 fields in each row") {
//    val f = fixture()
//    f.csvReader.drop(1).zipWithIndex.map{case (record, index) =>
//      println(index)
//      record.length should be (22)
//    }
//  }

  test("shoul d read the json correctly") {
    val f = fixture()
    val record = f.csvReader.drop(1).head
    record(18) should be ("{\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}")
  }
}
