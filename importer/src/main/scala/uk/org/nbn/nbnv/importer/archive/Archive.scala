package uk.org.nbn.nbnv.importer.archive

import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.{BadDataException, Options}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.{NbnRecord2, NbnRecord}
import uk.org.nbn.nbnv.utility.FileSystem

class Archive @Inject()(options: Options
                               , zipFileManager: ZipFileManager
                               , log: Logger
                               , metadataParser: ArchiveMetadataParser
                               , fs: FileSystem
                                , dfp: DataFileParser) {

  var isOpen = false

  def open()  {
    //Open zip file
    log.info("Opening archive %s".format(options.archivePath))
    val archiveFiles = zipFileManager.unZip(options.archivePath, options.tempDir)
    log.debug("Archive unzipped to temp folder %s".format(options.tempDir))

    //Read archive meta data file
    val xml = fs.loadXml(archiveFiles.metadata)
    val metadata = metadataParser.getMetadata(xml)
    log.debug("Read Metadata")

    dfp.open(archiveFiles.data, metadata)
    isOpen = true
  }

  def records() : Iterable[NbnRecord2] = {
    if (!isOpen) throw new IllegalStateException("The archive has not been opened")

    dfp.records
  }
}
