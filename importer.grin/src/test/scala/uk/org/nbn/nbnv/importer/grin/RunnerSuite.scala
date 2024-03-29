package uk.org.nbn.nbnv.importer.grin

import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.Settings
import uk.org.nbn.nbnv.importer.data.{QueryCache, Repository}
import org.apache.log4j.Logger
import java.net.URL

@RunWith(classOf[JUnitRunner])
class RunnerSuite extends FunSuite with ShouldMatchers with MockitoSugar with ResourceLoader {

  // change this from "ignore" to "test" to run
  ignore("run the grid importer") {

    val dataPath = resource("/gridrefs.csv")



    val options = Options(dataPath = dataPath.getFile, whatIf = true)

    val program = Program.create(options)
    program.run()
  }

  ignore("should import grids from a file") {
    val dataPath = new URL("file:///C://Working//grintest//NewCutDownGrids.csv")

    val options = Options(dataPath = dataPath.getFile, whatIf = true)

    val program = Program.create(options)
    program.run()
  }

  ignore("should be able to run a sproc") {

    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val r = new Repository(mock[Logger], em, mock[QueryCache])

//    val query = em.createQuery("SELECT t FROM Taxon t WHERE t.taxonVersionKey = :tvk", classOf[Taxon])
//    query.setParameter("tvk", taxonVersionKey)
//
//    query.getResultList.size == 1

//    key should startWith ("GA")
//    key should have length 8
  }
}
