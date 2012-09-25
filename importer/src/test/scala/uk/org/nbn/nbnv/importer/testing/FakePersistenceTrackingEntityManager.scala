package uk.org.nbn.nbnv.importer.testing

import collection.mutable.ArrayBuffer


class FakePersistenceTrackingEntityManager extends FakeEntityManagerBase {

  val buffer = new ArrayBuffer[Any]

  override def persist(e: Any) {
    buffer += e
  }
}
