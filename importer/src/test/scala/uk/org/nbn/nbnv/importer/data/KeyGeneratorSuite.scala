package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import javax.persistence.EntityManager


class KeyGeneratorSuite extends BaseFunSuite {

  def fixture = new {
    val repo = mock[CoreRepository]
    val stagingRepo = mock[StagingRepository]
    val db = new Database(mock[EntityManager], mock[EntityManager], stagingRepo, repo, mock[QueryCache])
  }

  test("when there are no existing datasets, should generate GA000001") {

    val f = fixture
    when(f.repo.getLatestDatasetKey).thenReturn(None)

    val g = new KeyGenerator(f.db)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000001")
  }

  test("when there are 3 existing datasets, should generate GA000004") {

    val f = fixture
    when(f.repo.getLatestDatasetKey).thenReturn(Some("GA000003"))

    val g = new KeyGenerator(f.db)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000004")
  }

  test("when there are 9 existing datasets, should generate GA000010") {

    val f = fixture
    when(f.repo.getLatestDatasetKey).thenReturn(Some("GA000009"))

    val g = new KeyGenerator(f.db)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000010")
  }

  test("when there are 1150 existing datasets, should generate GA001151") {

    val f = fixture
    when(f.repo.getLatestDatasetKey).thenReturn(Some("GA001150"))

    val g = new KeyGenerator(f.db)
    val result = g.nextTaxonDatasetKey
    result should be ("GA001151")
  }
}
