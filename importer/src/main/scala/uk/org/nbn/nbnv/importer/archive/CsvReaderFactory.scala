package uk.org.nbn.nbnv.importer.archive

import java.io.File

class CsvReaderFactory {
  def openCsvReader(filePath : String) = {
    val file = new File(filePath)
    new CsvReader(file)
  }
}
