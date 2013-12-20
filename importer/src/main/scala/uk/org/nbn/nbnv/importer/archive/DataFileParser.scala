package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.records.NbnRecord
import io.Source

class DataFileParser {
  var isOpen = false
  var lines : Iterator[String] = _
  var metadata : ArchiveMetadata = _

  def open(dataFilePath: String, metadata: ArchiveMetadata) {
    this.metadata = metadata
    lines = Source.fromFile(dataFilePath).getLines.drop(metadata.skipHeaderLines.getOrElse(0))

    isOpen = true
  }

  def records : Iterator[NbnRecord] = {
    if (!isOpen) throw new IllegalStateException("The data file has not been opened")

    lines.map {l =>
      var record = l.split(metadata.fieldSeparator)

      new NbnRecord(null)
    }

  }
}
