package uk.org.nbn.nbnv.importer

import data.{TaxonDatasetRepository, KeyGenerator}
import testing.BaseFunSuite
import utility.ResourceLoader
import uk.org.nbn.nbnv.PersistenceUtility
import org.mockito.Mockito._
import java.io.InputStream

/// This is an end-to-end test suite which requires the database.

class SmokeIntegrationSuite extends BaseFunSuite with ResourceLoader {

  ignore("should be able to get next dataset key") {

    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val dr = new TaxonDatasetRepository(em)
    val kg = new KeyGenerator(dr)

    val key = kg.nextTaxonDatasetKey

    key should startWith ("GA")
    key should have length 8
  }

  ignore("should import a valid archive") {

    val archivePath = resource("/archives/valid.zip")
    val options = Options(archivePath = archivePath.getFile, tempDir = ".\\temp")

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
