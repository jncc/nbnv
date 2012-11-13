package uk.org.nbn.nbnv.importer.logging

import org.apache.log4j.FileAppender
import java.io.File
import java.text.SimpleDateFormat
import org.apache.log4j.spi.ErrorCode
import java.util.Date


/**
* Adapted from http://veerasundar.com/blog/2009/08/how-to-create-a-new-log-file-for-each-time-the-application-runs/
*/
class NewLogPerRunFileAppender extends FileAppender {

  override def activateOptions() {
    if (fileName != null) {
      try {
        fileName = getLogFileName
        setFile(fileName, fileAppend, bufferedIO, bufferSize)
      } catch {
        case e: Exception => errorHandler.error("Error while activating log options", e, ErrorCode.FILE_OPEN_FAILURE)
      }
    }
  }

  def getLogFileName = {

    val timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())

    val file = new File(fileName)
    val name = file.getName
    val dotIndex = name.lastIndexOf(".")

    if (dotIndex != -1)
      // the file name has an extension, so insert the timestamp just before it
      file.getParent + File.separator + name.substring(0, dotIndex) + "." + timestamp + "." + name.substring(dotIndex + 1)
    else
      // the file name has no extension, so just append the timestamp at the end
      file.getParent + File.separator + name + timestamp
  }
}

