package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import collection.mutable.Map

class ZipFileManagerSuite extends BaseFunSuite with BeforeAndAfter with ResourceLoader  {
  var zfm : ZipFileManager = _
  var result = Map.empty[String, String]

  before{
    val logger = mock[Logger]
    zfm = new ZipFileManager(logger)

    val archive = resource("/archives/valid.zip")

    result = zfm.unZip(archive.getFile, "./temp")
  }

  test("should extract 3 elements") {
    result.size should be (3)
  }

  test("should extract meta.xml") {
    result.get("meta.xml") should not be (None)
  }

  test("should extract eml.xml") {
    result.get("eml.xml") should not be (None)
  }

  test("should extract data.tab") {
    result.get("data.tab") should not be (None)
  }
}
