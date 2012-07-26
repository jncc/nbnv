package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j._


class log { 
   val importLogger = "uk.org.nbn.nbnv.importer"
  
   def configureImportLog(logPath : String, maxLogSize : String ) = {
    val fa = new RollingFileAppender
    fa.setMaxFileSize(maxLogSize)
    fa.setName("FileLogger")
    fa.setFile(logPath)
    fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"))
    fa.setThreshold(Level.ALL)
    fa.setAppend(true)
    fa.activateOptions()

    Logger.getLogger(importLogger).addAppender(fa)
   }
  
  /*def configureImportLog() = {
   val fa = new FileAppender();
   fa.setName("FileLogger");
   fa.setFile("c:\\working\\myImportLog.log");
   fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
   fa.setThreshold(Level.ALL);
   fa.setAppend(true);
   fa.activateOptions();
   
   //add appender to any Logger (here is root)
   Logger.getLogger(importLogger).addAppender(fa)
   }*/
  
  def getImportLog() : Logger = {
    Logger.getLogger(importLogger)
  } 
}
