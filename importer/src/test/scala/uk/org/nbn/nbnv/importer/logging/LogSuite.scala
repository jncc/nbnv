package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j._
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class LogSuite extends BaseFunSuite {
  test("the log should work"){

    Log.configure(".", "2MB", "ALL")
    Log.get().info("The log is working!")
  }
}
