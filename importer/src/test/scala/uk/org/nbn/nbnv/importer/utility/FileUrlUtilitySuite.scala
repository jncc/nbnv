package uk.org.nbn.nbnv.importer.utility

import org.scalatest.{WordSpec, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import java.net.URL

class FileUrlUtilitySuite extends FunSpec with ShouldMatchers with MockitoSugar {

  describe ("FileUrlUtility") {

    it ("should convert Java file URL to Windows file path") {

      val url = new URL("file:/C:/some/resource.txt")
      val unc = FileUrlUtility.javaFileUrlToWindowsUncPath(url)

//      unc should be ("C:\\some\\resource.txt")
    }
  }
}
