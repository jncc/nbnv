package uk.org.nbn.nbnv.importer.archive

import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.Options
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.utility.FileSystem

class Archive @Inject()(options: Options
                               , zipFileManager: ZipFileManager
                               , log: Logger
                               , metadataParser: ArchiveMetadataParser
                               , fs: FileSystem
                               , dfp: DataFileParser) {

  private var isOpen = false
  private var metadata : ArchiveMetadata = _
  private var archiveFiles : ArchiveFilePaths = _

  def open(archivePath: String)  {
    //Open zip file
    log.info("Opening archive %s".format(archivePath))
    archiveFiles = zipFileManager.unZip(archivePath, options.tempDir)
    log.debug("Archive unzipped to temp folder %s".format(options.tempDir))

    //Read archive meta data file
    val xml = fs.loadXml(archiveFiles.archiveMetadata)
    metadata = metadataParser.getMetadata(xml)
    log.debug("Read Metadata")

    dfp.open(archiveFiles.data, metadata)
    log.debug("Opened data file")
    isOpen = true
  }

  def records() : Iterable[NbnRecord] = {
    if (!isOpen) throw new IllegalStateException("The archive has not been opened")

    dfp.records
  }

  def getArchiveMetadata() : ArchiveMetadata = {
    if (!isOpen) throw new IllegalStateException("The archive has not been opened")

    metadata
  }

  def getArchiveFiles() : ArchiveFilePaths = {
    if (!isOpen) throw new IllegalStateException("The archive has not been opened")

    archiveFiles
  }
}
