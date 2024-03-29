package uk.org.nbn.nbnv.importer

import testing.BaseFunSuite

class OptionsSuite extends BaseFunSuite {

  val archivePath = "c:\\some\\archive.zip"
  val logDir = "c:\\logs"
  val tempDir = "c:\\temp"
  val target = Target.validate
  val flush = "200"

  test("valid command line options should parse") {

    val valid = List(archivePath, "-target", target.toString, "-logDir", logDir, "-tempDir", tempDir, "-flush", flush)
    val result = Options.parse(valid)

    assertValid(result)
  }

  test("valid command line options passed in a weird order should parse") {

    val valid = List("-logDir", logDir, "-flush", flush , "-tempDir", tempDir, "-target", Target.validate.toString, archivePath)
    val result = Options.parse(valid)

    assertValid(result)
  }

  test("empty argument list should return usage") {

    val empty = List()
    val result = Options.parse(empty)

    result match {
      case OptionsFailure(message) => message.trim should startWith ("Usage")
      case _ => fail()
    }
  }

  test("should default to flush value of 25 if no argument is provided") {
    val valid = List("-logDir", logDir, "-tempDir", tempDir, "-target", Target.validate.toString, archivePath)
    val result = Options.parse(valid)

    result match {
      case OptionsSuccess(options) => {
        options.flush should be (25)
      }
      case _ => fail()
    }
  }

  def assertValid(result: OptionsResult) {
    result match {
      case OptionsSuccess(options) => {
        options.archivePath should be (archivePath)
        options.logDir should be (logDir)
        options.tempDir should be (tempDir)
        options.target should be (Target.validate)
        options.flush should be (flush.toInt)
      }
      case _ => fail()
    }
  }

}
