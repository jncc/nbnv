package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j._

object Log {


  def configure(logPath : String, maxLogSize : String, level: Level) = {

    val pattern = new PatternLayout("%d %-5p [%c{1}] %m%n")
    val fa = new RollingFileAppender
    fa.setMaxFileSize(maxLogSize)
    fa.setName("FileLogger")
    fa.setFile(logPath)
    fa.setLayout(pattern)
    fa.setThreshold(Level.ALL)
    fa.setAppend(true)
    fa.activateOptions()

    val ca = new ConsoleAppender
    ca.setName("ConsoleLogger")
    ca.setLayout(pattern)
    ca.setThreshold(Level.ALL)
    ca.activateOptions()

    getLog.addAppender(ca)
    getLog.addAppender(fa)
  }
  

  def getLog() = Logger.getLogger("uk.org.nbn.nbnv.importer")
}
