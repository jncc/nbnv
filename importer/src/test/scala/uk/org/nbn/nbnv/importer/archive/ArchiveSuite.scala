package uk.org.nbn.nbnv.importer.archive

import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.{Target, Options}
import org.apache.log4j.Logger
import xml.Elem
import uk.org.nbn.nbnv.utility.FileSystem
import uk.org.nbn.nbnv.importer.records.NbnRecord

class ArchiveSuite extends BaseFunSuite {
  def fixture = new {
    val log = mock[Logger]

    val archivePath =  "some/path/archive.zip"
    val options = Options(archivePath = archivePath, target = Target.commit, tempDir = "someTempDir")

    val files = new ArchiveFilePaths {
      val metadata: String =  "metadataPath"
      val data: String = "dataPath"
      val archiveMetadata: String = "archiveMetadataPath"
    }
    val zfm = mock[ZipFileManager]
    when(zfm.unZip(archivePath, options.tempDir)) thenReturn(files)

    val xml = mock[Elem]

    val fileSystem = mock[FileSystem]
    when(fileSystem.loadXml(files.archiveMetadata)).thenReturn(xml)

    val archiveMetadata = mock[ArchiveMetadata]
    val metadataParser = mock[ArchiveMetadataParser]
    when(metadataParser.getMetadata(xml)).thenReturn(archiveMetadata)

    val records = mock[Iterable[NbnRecord]]
    val dfp = mock[DataFileParser]
    when(dfp.records).thenReturn(records)

    val archive = new Archive(options,zfm, log, metadataParser, fileSystem, dfp)
  }

  test("should initialise correctly when an archive is opened") {
    val f = fixture

    f.archive.open(f.options.archivePath)

    //verfiy the calls are placed correctly
    verify(f.zfm).unZip(f.archivePath, f.options.tempDir)
    verify(f.fileSystem).loadXml(f.files.archiveMetadata)
    verify(f.metadataParser).getMetadata(f.xml)
    verify(f.dfp).open(f.files.data, f.archiveMetadata)

    //verify global properties have been set
    val metadata = f.archive.getArchiveMetadata()
    metadata should be (f.archiveMetadata)

    val files = f.archive.getArchiveFiles()
    files should be (f.files)

  }

  test("should return itterator if archive has been opened") {
    val f = fixture

    f.archive.open(f.archivePath)
    val r = f.archive.records

    r should be (f.records)
  }

  test("should fail if trying to read the archive when it has not been opened") {
    val f = fixture

    intercept[IllegalStateException] {
      f.archive.records
    }

    intercept[IllegalStateException] {
      f.archive.getArchiveMetadata
    }

    intercept[IllegalStateException] {
      f.archive.getArchiveFiles
    }
  }
}
