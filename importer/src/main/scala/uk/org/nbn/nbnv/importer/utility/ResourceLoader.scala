package uk.org.nbn.nbnv.importer.utility

import java.io.FileNotFoundException


/// Loads a java resource from the package.

trait ResourceLoader {
  def resource(path: String) = Option(getClass.getResource(path)) getOrElse { throw new FileNotFoundException(path) }
}

