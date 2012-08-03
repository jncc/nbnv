package uk.org.nbn.nbnv.importer

import testing.BaseFunSuite
import utility.ResourceLoader

/// This is an end-to-end test which requires the database.

class SmokeSuite extends BaseFunSuite with ResourceLoader {

  ignore("should import a valid archive") {

    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.getFile,
                          tempDir = ".\\temp")

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
