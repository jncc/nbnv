
package uk.org.nbn.nbnv

import jpa.nbncore.{Dataset, TaxonDataset}
import javax.persistence.EntityManager
import io.Source

object Importer {

  val datasetKey = "TESTPETE"
  val datasetTitle = "JUnit Test dataset"
  val dataPath = "C:\\Work\\nbnv-example-dataset\\TESTDS01.txt"

  def main(args: Array[String]) {
    for(line <- Source.fromFile(dataPath).getLines.drop(1).take(10)) {
      val record = new RecordParser().parse(line)
      println("parsed record " + record.recordKey)
    }
  }

  def createEntityManager() : EntityManager = {
    val u = new PersistenceUtility()
    val f = u.createEntityManagerFactory()
    f.createEntityManager()
  }

  def connectToDatabase() {
    val em = createEntityManager()
    em.getTransaction().begin()
    TaxonDatasetLoader.upsertTaxonDataset(em, datasetKey, datasetTitle);
    em.getTransaction().commit()
  }
}
