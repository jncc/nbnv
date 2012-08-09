package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.{Site, Organisation}
import scala.collection.JavaConversions._
import uk.org.nbn.nbnv.importer.data.Implicits._

class Repository(em: EntityManager) extends ControlAbstractions {

  def getOrganisation(name: String) = {

    val q = "select o from Organisation o where o.organisationName = :name "

    val query = em.createQuery(q, classOf[Organisation])
    query
      .setParameter("name", name)
      .setMaxResults(1)

    expectSingleResult(name) {
      query.getResultList
    }
  }

  def getSite(siteKey: String) = {

    val q = "select s from Site s where s.siteKey = :siteKey "

    val query = em.createQuery(q, classOf[Site])
    query
      .setParameter("siteKey", siteKey)
      .setMaxResults(1)

    expectSingleResult(siteKey) {
      query.getResultList
    }
  }

  def getLatestTaxonDatasetKey : Option[String] = {

    val q = "select d.datasetKey from TaxonDataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    em.createQuery(q, classOf[String]).getFirstOrNone
  }

}
