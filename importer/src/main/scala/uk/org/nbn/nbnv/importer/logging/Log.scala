package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j._
import java.io.File

object Log {

  def configure(logDir : String, maxLogSize : String, level: Level) = {

    val pattern = new PatternLayout("%d{yyyy-MMM-dd HH:mm:ss} %-5p %m%n")

    val fa = new RollingFileAppender
    val path = new File(logDir, "log.txt").getAbsolutePath
    fa.setMaxFileSize(maxLogSize)
    fa.setName("FileLogger")
    fa.setFile(path)
    fa.setLayout(pattern)
    fa.setThreshold(Level.ALL)
    fa.setAppend(true)
    fa.activateOptions()

    val ca = new ConsoleAppender
    ca.setName("ConsoleLogger")
    ca.setLayout(pattern)
    ca.setThreshold(Level.ALL)
    ca.activateOptions()

    get().addAppender(ca)
    get().addAppender(fa)
  }

  /// Gets the user-facing logger for the importer.
  def get() = Logger.getLogger("uk.org.nbn.nbnv.importer")
}
