package uk.org.nbn.nbnv.importer

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import testing.ResourceLoader

@RunWith(classOf[JUnitRunner])
class SmokeSuite extends FunSuite with ShouldMatchers with ResourceLoader {

  test("should be able to load a resource for automated testing") {
    val r = resource("/some-resource.txt")
//    1 should be (2)
    r should not be (null)
  }

  ignore("should import a valid archive") {
    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.toString)

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
