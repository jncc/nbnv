package uk.org.nbn.nbnv.importer.testing

trait ResourceLoader {

  def resource(path: String) = getClass.getResource(path)

}
