package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore.Site
import javax.persistence.EntityManager
import scala.collection.JavaConversions._


class SiteRepository(em : EntityManager) extends ControlAbstractions {

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
}
