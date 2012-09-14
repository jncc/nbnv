package uk.org.nbn.nbnv.importer.data

import org.apache.log4j.Logger
import com.google.inject.Inject


class QueryCache @Inject()(log: Logger) {

  val map = scala.collection.mutable.Map[String, Any]()

  def get[T](key: String) =  {

    log.debug("Query cache has %d objects".format(map.size))

    map.get(key).map(_.asInstanceOf[T])
  }

  def put(key: String, o: Any) {

    // simple caching policy to begin with - if the cache gets too big, clear it!
    if (map.size > 1000) {
      map.clear()
    }

    map.put(key, o)
  }
}
