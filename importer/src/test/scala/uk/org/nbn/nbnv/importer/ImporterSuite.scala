package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.apache.log4j.Logger
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.metadata.MetadataReader

@RunWith(classOf[JUnitRunner])
class ImporterSuite extends FunSuite with ShouldMatchers {

  test("importer should say hello") {
    val options = new Options {
      val archivePath = "."
      val tempDir = "."
      val logDir = "."
      val whatIf = false
    }
    val log = mock(classOf[Logger])
    val archiveManager = mock(classOf[ArchiveManager])
    val metadataReader = mock(classOf[MetadataReader])

    val importer = new Importer(options, log, archiveManager, metadataReader)
    importer.run()

    verify(log).info(startsWith("Welcome"))
  }

}
