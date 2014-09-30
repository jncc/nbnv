package uk.org.nbn.nbnv.importer.reset

import scala.collection.JavaConversions._
import scala.sys.process._

class DatasetReset() {
  def resetDatasetAccess(datasetKey : String) {
    val reset = "java -jar 'NBNV-AccessReseter-jar-with-dependencies.jar' %s".format(datasetKey)
    val status = reset.!
    if (status != 0) {
      throw new RuntimeException("failed to import")
    }
  }
}
