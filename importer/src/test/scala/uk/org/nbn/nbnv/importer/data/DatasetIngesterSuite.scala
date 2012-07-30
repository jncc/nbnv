package uk.org.nbn.nbnv.importer.data

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.persistence.EntityManager
import org.mockito.Mockito

@RunWith(classOf[JUnitRunner])
class DatasetIngesterSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  test("existing dataset should be updated") {

//    val file = mock(classOf[File])
//    when(file.getCanonicalPath).thenReturn(path)
    val em = mock[EntityManager]

    val injester = new DatasetIngester(em)
  }

}
