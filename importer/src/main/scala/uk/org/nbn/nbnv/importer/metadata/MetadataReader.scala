package uk.org.nbn.nbnv.importer.metadata

import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.utility.FileSystem
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.metadata.MetadataParser

class MetadataReader @Inject()(fs: FileSystem, parser: MetadataParser) {

  def read(archive: Archive): Metadata = {

    val file = archive.getMetadataLocationFile // seems we can't just get the path
    val path = file.getCanonicalPath
    val xml = fs.loadXml(path)

    parser.parse(xml)
  }
}
