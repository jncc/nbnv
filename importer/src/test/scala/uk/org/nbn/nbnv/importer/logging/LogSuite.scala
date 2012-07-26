package uk.org.nbn.nbnv.importer.logging

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.apache.log4j._

@RunWith(classOf[JUnitRunner])
class LogSuite extends FunSuite with ShouldMatchers {
  test("the log should work"){
    val log = new log()
    log.configureImportLog("c:\\working\\testLog.log","2MB")
    log.getImportLog().info("Arrrrggghh")
  }
}
