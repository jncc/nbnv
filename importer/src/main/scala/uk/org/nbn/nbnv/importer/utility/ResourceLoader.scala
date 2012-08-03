package uk.org.nbn.nbnv.importer.utility


/// Loads a java resource from the package.

trait ResourceLoader {
  def resource(path: String) = getClass.getResource(path)
}

