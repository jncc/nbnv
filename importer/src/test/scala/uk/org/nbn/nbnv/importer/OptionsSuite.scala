package uk.org.nbn.nbnv.importer

import com.google.inject.{Inject, AbstractModule, Guice}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Inside, FunSuite}
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class OptionsSuite extends FunSuite with ShouldMatchers {

  val archivePath = "c:\\some\\archive.zip"
  val logDir = "c:\\logs"
  val tempDir = "c:\\temp"

  test("valid command line options should parse") {

    val valid = List(archivePath, "-logDir", logDir, "-tempDir", tempDir, "-whatIf")
    val result = Options.parse(valid)

    result match {
      case OptionsSuccess(options) => {
        options.archivePath should be (archivePath)
        options.logDir should be (logDir)
        options.tempDir should be (tempDir)
        options.whatIf should be (true)
      }
      case _ => fail()
    }
  }

  test("empty argument list should return usage") {

    val empty = List()
    val result = Options.parse(empty)

    result match {
      case OptionsFailure(message) => message.trim should startWith ("Usage")
      case _ => fail()
    }
  }

}
