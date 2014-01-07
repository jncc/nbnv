package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.records.NbnRecord
import io.Source
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.{BadDataException, Options}
import java.io
import io.File

class DataFileParser @Inject()(options: Options) {
  var isOpen = false
  var csvReader : CSVReader = _
  var metadata : ArchiveMetadata = _

  def open(dataFilePath: String, metadata: ArchiveMetadata) {
    this.metadata = metadata
    csvReader = new CSVReader(new File(dataFilePath),options)

    isOpen = true
  }

  def records : Iterator[NbnRecord] = {
    if (!isOpen) throw new IllegalStateException("The data file has not been opened")

    csvReader.iterator.map{l =>
      if (l.length != metadata.fields) throw new BadDataException("The record at row %d does not contain %d fields".format(0,metadata.fields))

      new NbnRecord(null)
    }
  }
}
