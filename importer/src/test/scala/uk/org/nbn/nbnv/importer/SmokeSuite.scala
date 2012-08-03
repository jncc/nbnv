package uk.org.nbn.nbnv.importer

import testing.{BaseFunSuite, ResourceLoader}
import java.io.File

class SmokeSuite extends BaseFunSuite with ResourceLoader {

  test("should be able to load a resource for automated testing") {

    // some-resource.txt is in source control in the test /resources folder
    val r = resource("/some-resource.txt")
    r should not be (null)

//    println(r.toString)
//    val f = new File(r.toString)
//    println(f)
//    val source = io.Source.fromFile("C:\\Work\\nbnv\\importer\\target\\test-classes\\some-resource.txt") // (f.getPath)
//    val line = source.getLines().toTraversable.head
//    println(line)
  }

  ignore("should import a valid archive") {

    val archivePath = resource("/archives/valid.zip")

    val options = Options(archivePath = archivePath.toString)

    val importer = Importer.createImporter(options)
    importer.run()
  }
}
