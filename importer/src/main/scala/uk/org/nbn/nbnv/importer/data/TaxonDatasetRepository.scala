package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Implicits._

class TaxonDatasetRepository(em: EntityManager) {

  def getLatestTaxonDatasetKey : Option[String] = {

    val q = "select d.datasetKey from TaxonDataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    em.createQuery(q, classOf[String]).singleOrNone
  }
}
