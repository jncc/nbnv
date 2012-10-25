package uk.org.nbn.nbnv.importer.smoke

import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer.Options
import uk.org.nbn.nbnv.importer.Importer

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

  test("should import a valid archive generated by the importer UI") {

    val f = fixture("/archives/valid_ui.zip")
    f.importer.run()
  }
}
