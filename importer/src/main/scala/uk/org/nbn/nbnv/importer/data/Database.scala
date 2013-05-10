package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager

/// A wrapper data access object, or data context.
class Database(val em: EntityManager, val sem: EntityManager, val stagingRepo: StagingRepository, val coreRepo: CoreRepository, val cache: QueryCache) {

  def flushAndClear() {
    // flush and clear the entity manager to prevent slowdown and memory consumption
    em.flush()
    em.clear()

    sem.flush()
    sem.clear()

    // clear the cache so it will not hold on to objects which are no longer tracked
    cache.clear()
    //todo: need to clear the staging cache too.
  }

}
