package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.records.NbnRecord
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.Options
import java.io.File
import org.apache.log4j.Logger

class DataFileParser @Inject()(options: Options
                               , recordFactory : NbnRecordFactory
                               , log : Logger
                               , readerFactory : CsvReaderFactory) {
  var isOpen = false
  var csvReader : CsvReader = _
  var metadata : ArchiveMetadata = _

  def open(dataFilePath: String, metadata: ArchiveMetadata) {
    this.metadata = metadata
    csvReader = readerFactory.openCsvReader(dataFilePath)

    isOpen = true
  }

  def records : Iterable[NbnRecord] = {
    if (!isOpen) throw new IllegalStateException("The data file has not been opened")

    csvReader.drop(metadata.skipHeaderLines.getOrElse(0)).zipWithIndex.map{ case (rawData, i) =>
      if (rawData.length < metadata.fields) {
        log.warn("The record at row %d contains less fields then the %d mapped fields".format(i + 1,metadata.fields))
      }

      recordFactory.makeRecord(rawData, metadata)
    }
  }
}
