package uk.org.nbn.nbnv.importer

import testing.BaseFunSuite
import uk.org.nbn.nbnv.utility.FileSystem
import org.mockito.Mockito._

class ExampleSuite extends BaseFunSuite {

  test("can mock with mockito") {

    val xml = <hello>world</hello>

    val fs = mock[FileSystem]
    when(fs.loadXml("bob")).thenReturn(xml)

    val result = fs.loadXml("bob")
    result should be (xml)
  }
}
