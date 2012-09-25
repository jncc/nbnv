package uk.org.nbn.nbnv.importer.testing

import javax.persistence.{FlushModeType, LockModeType, EntityManager}
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.util
import javax.persistence.criteria.CriteriaQuery

abstract class FakeEntityManagerBase extends EntityManager {

  def persist(p1: Any) {}

  def merge[T](p1: T) = throw new NotImplementedException

  def remove(p1: Any) {}

  def find[T](p1: Class[T], p2: Any) = throw new NotImplementedException

  def find[T](p1: Class[T], p2: Any, p3: util.Map[String, AnyRef]) = throw new NotImplementedException

  def find[T](p1: Class[T], p2: Any, p3: LockModeType) = throw new NotImplementedException

  def find[T](p1: Class[T], p2: Any, p3: LockModeType, p4: util.Map[String, AnyRef]) = throw new NotImplementedException

  def getReference[T](p1: Class[T], p2: Any) = throw new NotImplementedException

  def flush() {}

  def setFlushMode(p1: FlushModeType) {}

  def getFlushMode = null

  def lock(p1: Any, p2: LockModeType) {}

  def lock(p1: Any, p2: LockModeType, p3: util.Map[String, AnyRef]) {}

  def refresh(p1: Any) {}

  def refresh(p1: Any, p2: util.Map[String, AnyRef]) {}

  def refresh(p1: Any, p2: LockModeType) {}

  def refresh(p1: Any, p2: LockModeType, p3: util.Map[String, AnyRef]) {}

  def clear() {}

  def detach(p1: Any) {}

  def contains(p1: Any) = false

  def getLockMode(p1: Any) = null

  def setProperty(p1: String, p2: Any) {}

  def getProperties = null

  def createQuery(p1: String) = null

  def createQuery[T](p1: CriteriaQuery[T]) = null

  def createQuery[T](p1: String, p2: Class[T]) = null

  def createNamedQuery(p1: String) = null

  def createNamedQuery[T](p1: String, p2: Class[T]) = null

  def createNativeQuery(p1: String) = null

  def createNativeQuery(p1: String, p2: Class[_]) = null

  def createNativeQuery(p1: String, p2: String) = null

  def joinTransaction() {}

  def unwrap[T](p1: Class[T]) = throw new NotImplementedException

  def getDelegate = null

  def close() {}

  def isOpen = false

  def getTransaction = null

  def getEntityManagerFactory = null

  def getCriteriaBuilder = null

  def getMetamodel = null
}
