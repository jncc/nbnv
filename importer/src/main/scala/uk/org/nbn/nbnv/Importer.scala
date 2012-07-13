
package uk.org.nbn.nbnv

import jpa.nbncore.{Dataset, TaxonDataset}
import javax.persistence.EntityManager

object Importer {

  val datasetKey = "TESTJUTD"
  val datasetTitle = "JUnit Test dataset"

  def main(args: Array[String]) {
    println("Hello, world!")

    val em = createEntityManager()

    println("Goodbye, world!")
  }

  def createEntityManager() : EntityManager = {
    val u = new PersistenceUtility()
    val f = u.createEntityManagerFactory()
    f.createEntityManager()
  }

//  def blah() {
//    println("deleteTaxonDataset");
//
//    val _em.getTransaction().begin()
//    TaxonDatasetLoader.deleteTaxonDataset(_em, _datasetKey);
//    _em.getTransaction().commit();
//
//    TaxonDataset td = _em.find(TaxonDataset.class, _datasetKey);
//    assertNull("TaxonDataset entry not removed", td);
//    Dataset d = _em.find(Dataset.class, _datasetKey);
//    assertNull("Dataset entry not removed", d)
//  }
}
