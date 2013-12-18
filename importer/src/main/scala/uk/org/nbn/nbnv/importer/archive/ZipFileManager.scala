package uk.org.nbn.nbnv.importer.archive

import java.util.zip.ZipFile
import java.io.FileInputStream
import java.io.FileOutputStream
import scala.collection.JavaConversions._
import java.util.zip.ZipEntry
import java.io.InputStream
import java.io.OutputStream
import java.io.File
import com.google.inject.Inject
import org.apache.log4j.Logger
import collection.mutable.ListBuffer

class ZipFileManager @Inject()(log: Logger) {

  val BUFSIZE = 4096
  val buffer = new Array[Byte](BUFSIZE)

  def unZip(sourceFile: String, targetFolder: String) = {
    val zipFile = new ZipFile(sourceFile)
    var target = new File(targetFolder)
    //clear target folder
    if (target.exists()) {
      target.delete()
      target.mkdir()
    }


    unzipAllFiles(zipFile, getZipEntryInputStream(zipFile)_,target )
  }

  private def getZipEntryInputStream(zipFile: ZipFile)(entry: ZipEntry) = zipFile.getInputStream(entry)

  private def unzipAllFiles(zipFile: ZipFile, inputGetter: (ZipEntry) => InputStream, targetFolder: File): List[File] = {
    val unzippedFiles = new ListBuffer[File]

    zipFile.entries.foreach(entry => {
      if (entry.isDirectory) {
        new File(targetFolder, entry.getName).mkdirs
      }
      else {
        val newFile = new File(targetFolder, entry.getName)
        saveFile(inputGetter(entry), new FileOutputStream(newFile))
        unzippedFiles.append(newFile)
      }
    })

    return unzippedFiles.toList
  }

  private def saveFile(fis: InputStream, fos: OutputStream) = {
    writeToFile(bufferReader(fis)_, fos)
    fis.close
    fos.close
  }

  private def bufferReader(fis: InputStream)(buffer: Array[Byte]) = (fis.read(buffer), buffer)

  private def writeToFile(reader: (Array[Byte]) => Tuple2[Int, Array[Byte]], fos: OutputStream): Boolean = {
    val (length, data) = reader(buffer)
    if (length >= 0) {
      fos.write(data, 0, length)
      writeToFile(reader, fos)
    } else
      true
  }
}

