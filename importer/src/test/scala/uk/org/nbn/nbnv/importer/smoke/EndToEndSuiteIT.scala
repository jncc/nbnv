package uk.org.nbn.nbnv.importer.smoke

import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer.{Target, BadDataException, Options, Importer}
import java.net.{URI, URL}

class EndToEndSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture(archiveURL: URL) = new {

    val archive = archiveURL
    val options = Options(archivePath = archive.getFile, target = Target.ingest)

    val importer = Importer.createImporter(options)
  }

  // change from 'ignore' to 'test' to run the importer against an archive within your IDE
  ignore("import an archive") {

    val archive = new URL("file:///C://Working//nbnv-234//nbnv-234.zip")
    val f = fixture(archive)
    f.importer.run()
  }

  test("should import a valid archive") {

    val archive = resource("/archives/valid.zip")
    val f = fixture(archive)
    f.importer.run()
  }

  ignore("should throw on non-existent dataset key") {

    val ex = intercept[BadDataException] {

      val archive = resource("/archives/nonexistent_dataset_key.zip")
      val f = fixture(archive)
      f.importer.run()
    }

    ex.message should include ("Dataset 'DOESNOTEXIST' does not exist")
  }


}
