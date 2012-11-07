package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j._
import java.io.File

object Log {

  def configure(logDir: String, archivePath: String, logLevel: String) {

    val level = Level.toLevel(logLevel)
    val pattern = new PatternLayout("%d{yyyy-MMM-dd HH:mm:ss} %-5p %m%n")

    val fa = new NewLogPerRunFileAppender
    val name =  new File(archivePath).getName.replace(' ', '-')
    val path = new File(logDir, "log-" + name + ".log").getAbsolutePath
    fa.setName("LogPerRunFileLogger")
    fa.setFile(path)
    fa.setLayout(pattern)
    fa.setThreshold(level)
    fa.setAppend(true)
    fa.activateOptions()

    val ca = new ConsoleAppender
    ca.setName("ConsoleLogger")
    ca.setLayout(pattern)
    ca.setThreshold(level)
    ca.activateOptions()

    get().addAppender(ca)
    get().addAppender(fa)
  }

  /// Gets the user-facing logger for the importer.
  def get() = Logger.getLogger("uk.org.nbn.nbnv.importer")
}
