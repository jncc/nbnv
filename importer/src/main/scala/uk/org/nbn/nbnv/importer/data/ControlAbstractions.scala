package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.BadDataException

trait ControlAbstractions {

  def expectSingleResult[T](identifier: Any)(f: => Iterable[T]): T = {

    val results = f

    if (results.isEmpty) {
      throw new BadDataException("Expected one result for '%s', but found none.".format(identifier))
    }
    else if (results.size == 1) {
      results.head
    }
    else {
      throw new BadDataException("Expected one result for '%s', but found %d.".format(identifier, results.size))
    }
  }


}
