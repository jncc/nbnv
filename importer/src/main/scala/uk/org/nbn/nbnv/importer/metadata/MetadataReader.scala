package uk.org.nbn.nbnv.importer.metadata

import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.utility.FileSystem
import com.google.inject.Inject

class MetadataReader @Inject()(fs: FileSystem, parser: MetadataParser) {

  def read(path: String): Metadata = {

    val xml = fs.loadXml(path)

    parser.parse(xml)
  }
}
