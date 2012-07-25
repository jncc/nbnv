package uk.org.nbn.nbnv.utility

package uk.org.nbn.nbnv

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._

@RunWith(classOf[JUnitRunner])
class FileSystemSuite extends FunSuite with ShouldMatchers  {
  // just checking i can do some mockin'
  test("can mock with mockito") {
    val fs = mock(classOf[FileSystem])
    val xml = <hello>world</hello>
    when(fs.loadXml("bob")).thenReturn(xml)

    val result = fs.loadXml("bob")
    result should be (xml)
  }
}



