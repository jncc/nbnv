package uk.org.nbn.nbnv.importer.smoke

import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer.{ImportFailedException, Options, Importer}

class EndToEndSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture(archivePath: String) = new {

    val tempDir = ".\\temp"
    new File(tempDir).mkdirs()

    val archive = resource(archivePath)
    val options = Options(archivePath = archive.getFile, tempDir = tempDir, whatIf = true)

    val importer = Importer.createImporter(options)
  }


  test("should import a valid archive") {

    val f = fixture("/archives/valid.zip")
    f.importer.run()
  }

  test("should throw on non-existent dataset key") {

    val ex = intercept[ImportFailedException] {

      val f = fixture("/archives/nonexistent_dataset_key.zip")
      f.importer.run()
    }

    ex.message should include ("Dataset 'DOESNOTEXIST' does not exist")
  }


}
