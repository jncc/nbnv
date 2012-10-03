package uk.org.nbn.nbnv.importer.grin

import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import uk.org.nbn.nbnv.importer.grin.Options

@RunWith(classOf[JUnitRunner])
class RunnerSuite extends FunSuite with ShouldMatchers with MockitoSugar with ResourceLoader {

  // change this from "ignore" to "test" to run
  test("run the grid importer") {

    val dataPath = resource("/gridrefs.csv")

    val options = Options(dataPath = dataPath.getFile, whatIf = true)

    val program = Program.create(options)
    program.run()
  }
}
