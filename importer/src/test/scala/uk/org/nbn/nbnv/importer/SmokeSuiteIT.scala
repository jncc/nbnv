package uk.org.nbn.nbnv.importer

import data.{QueryCache, Repository, KeyGenerator}
import testing.BaseFunSuite
import utility.ResourceLoader
import uk.org.nbn.nbnv.PersistenceUtility
import org.mockito.Mockito._
import java.io.{File, InputStream}
import org.apache.log4j.Logger

/// This is an end-to-end test suite which requires the database.

class SmokeSuiteIT extends BaseFunSuite with ResourceLoader {

  test("should be able to get next dataset key") {

    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val dr = new Repository(mock[Logger], em, mock[QueryCache])
    val kg = new KeyGenerator(dr)

    val key = kg.nextTaxonDatasetKey

    key should startWith ("GA")
    key should have length 8
  }

  test("should import a valid archive") {

    val tempDir = ".\\temp"
    new File(tempDir).mkdirs()
    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.getFile, tempDir = tempDir, whatIf = true)

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
