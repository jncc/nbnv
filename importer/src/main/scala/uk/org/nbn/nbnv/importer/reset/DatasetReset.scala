package uk.org.nbn.nbnv.importer.reset

import scala.collection.JavaConversions._
import scala.sys.process._
import uk.org.nbn.nbnv.importer.Settings

class DatasetReset() {
  def resetDatasetAccess(datasetKey : String) {
    val reset = "java -jar '%s' %s".format(Settings.datasetReset, datasetKey)
    val status = reset.!
    if (status != 0) {
      throw new RuntimeException("Failed to run dataset reset tool, please run manually");
    }
  }
}
