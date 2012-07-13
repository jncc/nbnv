
package uk.org.nbn.nbnv

import jpa.nbncore.{Dataset, TaxonDataset}
import javax.persistence.EntityManager

object Importer {

  val datasetKey = "TESTPETE"
  val datasetTitle = "JUnit Test dataset"

  def main(args: Array[String]) {
    println("Hello, world!")
    connectToDatabase()
    println("Goodbye, world!")
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

//    TaxonDataset td = _em.find(TaxonDataset.class, _datasetKey);
//    assertNull("TaxonDataset entry not removed", td);
//    Dataset d = _em.find(Dataset.class, _datasetKey);
//    assertNull("Dataset entry not removed", d)
  }
}
