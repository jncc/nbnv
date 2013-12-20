package uk.org.nbn.nbnv.importer.archive

import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.{BadDataException, Options}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.utility.FileSystem

class ArchiveManager @Inject()(options: Options
                               , zipFileManager: ZipFileManager
                               , log: Logger
                               , metadataParser: ArchiveMetadataParser
                               , fs: FileSystem) {

  def open()  {
    //Open zip file
    log.info("Opening archive %s".format(options.archivePath))
    val archiveFiles = zipFileManager.unZip(options.archivePath, options.tempDir)

    //Read archive meta data file
    val xml = fs.loadXml(archiveFiles.metadata)
    val metadata = metadataParser.getMetadata(xml)

  }

  def records() : Seq[NbnRecord] = { List[NbnRecord]() }
  //Read mapping
  //if has headers skip line on
  //read line into raw record using mapping
  //cl

}
