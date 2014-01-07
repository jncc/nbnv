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
import collection.mutable.Map

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

    val fileMap = unzipAllFiles(zipFile, getZipEntryInputStream(zipFile)_,target )

    new ArchiveFilePaths {
      val metadata: String = fileMap("eml.xml")
      val data: String = fileMap("data.tab")
      val fieldMap: String = fileMap("meta.xml")
    }

  }

  private def getZipEntryInputStream(zipFile: ZipFile)(entry: ZipEntry) = zipFile.getInputStream(entry)

  private def unzipAllFiles(zipFile: ZipFile, inputGetter: (ZipEntry) => InputStream, targetFolder: File): Map[String, String] = {
    val unzippedFiles = Map.empty[String,String]

    zipFile.entries.foreach(entry => {
      if (entry.isDirectory) {
        new File(targetFolder, entry.getName).mkdirs
      }
      else {
        val newFile = new File(targetFolder, entry.getName)
        saveFile(inputGetter(entry), new FileOutputStream(newFile))
        unzippedFiles += newFile.getName -> newFile.getAbsolutePath
        log.debug("Unzipped %s to %s".format(newFile.getName, newFile.getAbsolutePath))
      }
    })

    return unzippedFiles
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

