
package uk.org.nbn.nbnv

object Importer {

  def main(args: Array[String]) {
    println("Hello, world!")
    val datasetKey = "TESTJUTD"
    val datasetTitle = "JUnit Test dataset"
    val b = new PersistenceUtility()
    val f = b.createEntityManagerFactory()
    val em = f.createEntityManager()
    println(em.isOpen)
  }
}




