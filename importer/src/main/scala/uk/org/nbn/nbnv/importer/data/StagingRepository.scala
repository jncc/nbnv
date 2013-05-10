package uk.org.nbn.nbnv.importer.data

import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Implicits._
import uk.org.nbn.nbnv.jpa.nbnimportstaging.{StoredProcedureLibrary,TaxonDataset}
import uk.org.nbn.nbnv.importer.BadDataException

class StagingRepository(log: Logger, em: EntityManager, cache: QueryCache) extends ControlAbstractions {

  def deleteTaxonObservationsAndRelatedRecords(datasetKey: String) {
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.deleteTaxonObservationsAndRelatedRecords(datasetKey)
  }

  def getTaxonDataset(key: String) = {

    cacheSingle("getTaxonDataset", key) {
      em.findSingleOrNone(classOf[TaxonDataset], key) getOrElse {
        throw new BadDataException("Dataset '%s' does not exist. Please check the key is correct.".format(key))
      }
    }
  }
  /// Caches the result of the function f.
  // A bit nasty - cacheSome and cacheSingle are separate functions
  // because scala won't let us overload on f's type
  private def cacheSingle[T](cacheKey: String*)(f: => T) = {
    cacheSome(cacheKey:_*) { Some(f) }.get
  }

  private def cacheSome[T](cacheKey: String*)(f: => Option[T]): Option[T] = {

    for (item <- cacheKey) {
      if (item == null)
        throw new BadDataException("Cache key component in '%s' was null. This could lead to incorrect data.".format(cacheKey.mkString("|")))
    }

    val key = cacheKey.map(_.trim).mkString("|")
    log.debug("Query cache key is '%s'".format(key))

    cache.get(key) orElse {
      log.debug("Query cache miss for '%s'".format(key))
      // using the map function here means we only cache the results of
      // successful queries (that return Some(t)) - we don't want
      // to cache the results of unsuccessful queries; we want to go back
      // to the db next time
      f map { t =>
        cache.put(key, t)
        t
      }
    }
  }


}
