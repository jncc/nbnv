package uk.org.nbn.nbnv.importer

import testing.{BaseFunSuite, ResourceLoader}
import java.io.File

class SmokeSuite extends BaseFunSuite with ResourceLoader {

  test("should be able to load a resource for automated testing") {

    // some-resource.txt is in source control in the test /resources folder
    val r = resource("/some-resource.txt")
    r should not be (null)

    // let's actually read the resource
    val file = new File(r.getFile)
    val source = io.Source.fromFile(file)
    val firstLine = source.getLines().toTraversable.head

    firstLine should startWith ("This is a resource")
  }

  ignore("should import a valid archive") {

    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.getFile)

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
