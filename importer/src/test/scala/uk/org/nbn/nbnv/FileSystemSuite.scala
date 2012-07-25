package uk.org.nbn.nbnv

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._

@RunWith(classOf[JUnitRunner])
class TestFileSystemSuite extends FunSuite with ShouldMatchers  {
  // just checking i can mock this baby
  test("can mock") {
    val fs = mock(classOf[FileSystem])
    when(fs.loadXml("bob")).thenReturn("hello")

    val result = fs.loadXml("bob")
    println(result)
    result should be ("hello")
  }
}
