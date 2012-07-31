package uk.org.nbn.nbnv.metadata

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.gbif.dwc.text.Archive
import org.mockito.Mockito._
import uk.org.nbn.nbnv.utility.FileSystem
import java.io.File
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class MetadataReaderSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  test("should read metadata") {

    // arrange

    val path = "some/path"

    val file = mock[File]
    when(file.getCanonicalPath).thenReturn(path)

    val archive = mock[Archive]
    when(archive.getMetadataLocationFile).thenReturn(file)

    val fileSystem = mock[FileSystem]
    when(fileSystem.loadXml(path)).thenReturn(TestData.validMetadataXml)

    // act
    val reader = new MetadataReader(fileSystem, new MetadataParser)
    val metadata = reader.read(archive)

    // assert
    metadata should not be(null)
  }
}
