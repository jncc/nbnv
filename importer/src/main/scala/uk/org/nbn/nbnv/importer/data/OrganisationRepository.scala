package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.Organisation
import scala.collection.JavaConversions._
import uk.org.nbn.nbnv.importer.ImportException

class OrganisationRepository(em: EntityManager) {

  def getOrganisation(name: String) = {

    val q = "select o from Organisation o where o.organisationName = :name "

    val query = em.createQuery(q, classOf[Organisation])
    query.setParameter("name", name)
    val results = query.setMaxResults(1).getResultList

//    expectSingleResult(name) {
//      query.setMaxResults(1).getResultList
//    }
    if (results.isEmpty)
      throw new ImportException("Organisation '%s' not found.".format(name))
    else if (results.size == 1)
      results.head
    else
      throw new ImportException("The sequence contains more than one element.")
  }

  def expectSingleResult[T](id: String)(f: => List[T]) = {

    val results = f

    if (results.isEmpty)
      throw new ImportException("Entity '%s' not found.".format(id))
    else if (results.size == 1)
      results.head
    else
      throw new ImportException("The sequence contains more than one element.")
  }
}


