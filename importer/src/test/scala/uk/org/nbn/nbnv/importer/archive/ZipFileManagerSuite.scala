package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.utility.ResourceLoader

class ZipFileManagerSuite extends BaseFunSuite with BeforeAndAfter with ResourceLoader  {
  var zfm : ZipFileManager = _

  before{
    val logger = mock[Logger]
    zfm = new ZipFileManager(logger)
  }

  test("should extract valid zip") {
    val archive = resource("/archives/valid.zip")

    val result = zfm.unZip(archive.getFile, "./temp")
    result.length should be (3)
  }
}
