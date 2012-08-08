package uk.org.nbn.nbnv.importer.data

import javax.persistence.{NonUniqueResultException, TypedQuery}
import scala.collection.JavaConversions._

object Implicits {

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
  }

  implicit def query2RichTypedQuery[T](q: TypedQuery[T]) = new RichTypedQuery[T](q)
}
