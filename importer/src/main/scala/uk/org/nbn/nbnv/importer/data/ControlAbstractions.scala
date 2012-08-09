package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.ImportFailedException

trait ControlAbstractions {

  def expectSingleResult[T](identifier: String)(f: => Iterable[T]): T = {

    val results = f

    if (results.isEmpty) {
      throw new ImportFailedException("Expected one result for '%s', but found none.".format(identifier))
    }
    else if (results.size == 1) {
      results.head
    }
    else {
      throw new ImportFailedException("Expected one result for '%s', but found %d.".format(identifier, results.size))
    }
  }


}
