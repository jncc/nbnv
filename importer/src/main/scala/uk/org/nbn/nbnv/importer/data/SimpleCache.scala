package uk.org.nbn.nbnv.importer.data


class SimpleCache {

  val map = scala.collection.mutable.Map[String, Any]()

  def get[T](key: String) =  {

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
