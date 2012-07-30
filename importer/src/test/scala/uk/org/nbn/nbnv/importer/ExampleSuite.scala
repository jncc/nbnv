package uk.org.nbn.nbnv.importer

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import uk.org.nbn.nbnv.utility.FileSystem
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ExampleSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  test("can mock with mockito") {

    val xml = <hello>world</hello>

    val fs = mock[FileSystem]
    when(fs.loadXml("bob")).thenReturn(xml)

    val result = fs.loadXml("bob")
    result should be (xml)
  }
}
