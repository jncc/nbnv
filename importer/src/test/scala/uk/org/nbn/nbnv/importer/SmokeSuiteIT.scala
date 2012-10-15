package uk.org.nbn.nbnv.importer

import data.{Database, QueryCache, Repository, KeyGenerator}
import ingestion.FeatureIngester
import injection.ImporterModule
import testing.BaseFunSuite
import utility.ResourceLoader
import uk.org.nbn.nbnv.PersistenceUtility
import org.mockito.Mockito._
import java.io.{File, InputStream}
import org.apache.log4j.Logger
import com.google.inject.Guice

/// This is an end-to-end test suite which requires the database.

class SmokeSuiteIT extends BaseFunSuite with ResourceLoader {

  ignore("should be able to get next dataset key") {

    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val db = new Database(em, new Repository(mock[Logger], em, mock[QueryCache]), mock[QueryCache])
    val kg = new KeyGenerator(db)

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

  test("should import a valid archive, generated by importer UI") {
    val tempDir = ".\\temp"
    new File(tempDir).mkdirs()
    val archivePath = resource("/archives/valid_ui.zip")

    val options = Options(archivePath = archivePath.getFile, tempDir = tempDir, whatIf = true)

    val importer = Importer.createImporter(options)
    importer.run()
  }


  ignore("should ensure gridrefs are of a correct format / correct precision in this case") {
    val tempDir = ".\\temp"
    new File(tempDir).mkdirs()
    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.getFile, tempDir = tempDir, whatIf = true)

    val injector = Guice.createInjector(new ImporterModule(options))

    val ingester = injector.getInstance(classOf[FeatureIngester])

    ingester.ensureGridRefFeature("NN166712", "OSGB", 100)
  }
}
