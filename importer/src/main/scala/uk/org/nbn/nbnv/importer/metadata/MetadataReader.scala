package uk.org.nbn.nbnv.metadata

import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.utility.FileSystem

class MetadataReader(fs: FileSystem, parser: MetadataParser) {
  def read(archive: Archive): Metadata = {
    val file = archive.getMetadataLocationFile() // seems we can't just get the path
    val path = file.getCanonicalPath()
    val xml = fs.loadXml(path)

    parser.parse(xml)
  }
}
