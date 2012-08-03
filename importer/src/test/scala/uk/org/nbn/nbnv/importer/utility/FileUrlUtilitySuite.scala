package uk.org.nbn.nbnv.importer.utility

import java.net.URL
import uk.org.nbn.nbnv.importer.testing.BaseFunSpec

class FileUrlUtilitySuite extends BaseFunSpec {

  describe ("FileUrlUtility") {

    it ("should convert Java file URL to Windows file path") {

      // this is the sort of url produced by getClass.getResource(path)
      val sillyJavaMalformedFileUrl = "file:/C:/some/resource.txt"
      val url = new URL(sillyJavaMalformedFileUrl)

      // check that the URL class hasn't parsed/altered it
      url.toString should be (sillyJavaMalformedFileUrl)

      val unc = FileUrlUtility.javaFileUrlToWindowsUncPath(url)
//      unc should be ("C:\\some\\resource.txt")
    }
  }
}
