package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import scala.collection.JavaConversions._

class TaxonDatasetRepository(em: EntityManager) {

  def getLatestTaxonDatasetKey : Option[String] = {

    val q = "select d.datasetKey from TaxonDataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    // todo: move grunt code into getSingleResultOrNone extension
    val query = em.createQuery(q, classOf[String])
    val results = query.setMaxResults(1).getResultList

    if (results.isEmpty)
      None
    else if (results.size == 1)
      Some(results.head)
    else
      error("The sequence contains more than one element.")
  }
}

//object MyExtensions {
//
//  class RichEntityManager(em: EntityManager) {
//
//    type T
//
//    def getSingleResultOrNone(query: Query) : Option[T] = {
//
//    if (results.isEmpty)
//      None
//    else if (results.size == 1)
//      Some(results.head)
//    else
//      error("The sequence contains more than one element.")
//    }
//  }
//  implicit def richEntityManager(em: EntityManager) = new RichEntityManager(em)
//}
