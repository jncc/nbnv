package uk.org.nbn.nbnv.importer.data

import javax.persistence.{EntityManager, NonUniqueResultException, TypedQuery}
import scala.collection.JavaConversions._

object Implicits extends ControlAbstractions {

  implicit def entityManager2RichEntityManager(em: EntityManager) = new RichEntityManager(em)

  class RichEntityManager(em: EntityManager) {

    def findSingle[T](c: Class[T], id: Any) = {
      expectSingleResult(id) {

        val t = em.find(c, id)

        if (t == null) List()
        else List(t)
      }
    }
  }

  implicit def typedQuery2RichTypedQuery[T](q: TypedQuery[T]) = new RichTypedQuery[T](q)

  class RichTypedQuery[T](q: TypedQuery[T]) {

    def getSingleOrNone : Option[T] = {

      val results = q.setMaxResults(2).getResultList

      if (results.isEmpty)
        None
      else if (results.size == 1)
        Some(results.head)
      else
        throw new NonUniqueResultException("The sequence contains more than one element.")
    }

    def getFirstOrNone : Option[T] = {

      val results = q.setMaxResults(1).getResultList

      if (results.isEmpty)
        None
      else
        Some(results.head)
    }
  }
}
