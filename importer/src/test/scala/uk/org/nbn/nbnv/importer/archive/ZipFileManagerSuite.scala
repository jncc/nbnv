package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import java.net.URL

class ZipFileManagerSuite extends BaseFunSuite with BeforeAndAfter with ResourceLoader  {
  var zfm : ZipFileManager = _
  var archive : URL = _

  before{
    val logger = mock[Logger]
    zfm = new ZipFileManager(logger)

    archive = resource("/archives/valid.zip")
  }

  test("should extract meta.xml") {
    val result = zfm.unZip(archive.getFile, "./temp")
    result.archiveMetadata should endWith ("meta.xml")
    result.metadata should endWith ("eml.xml")
    result.data should endWith ("data.tab")
  }

}
