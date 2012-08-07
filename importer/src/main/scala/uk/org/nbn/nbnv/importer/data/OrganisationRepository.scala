package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.Organisation
import scala.collection.JavaConversions._

class OrganisationRepository(em: EntityManager) extends ControlAbstractions {

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
}


