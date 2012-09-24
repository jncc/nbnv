package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.metadata.Metadata
import org.mockito.Mockito._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Repository}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.jpa.nbncore.{GridSquare, Feature}

class FeatureIngesterSuite extends BaseFunSuite {

  def fixture = new {
    val gridRef = "ABCDEF"
    val gridReferenceType = "OSGBTEST"
    val gridReferencePrecision = 12345

    val gridSquareInfo = mock[GridSquareInfo]
    when(gridSquareInfo.gridReference).thenReturn(gridRef)
    val gridSquareInfoFactory = mock[GridSquareInfoFactory]
    when(gridSquareInfoFactory.getGridSquare(gridRef, gridReferenceType, gridReferencePrecision))
      .thenReturn(gridSquareInfo)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(gridRef))
    when(record.gridReferenceType).thenReturn(Some(gridReferenceType))
    when(record.gridReferencePrecision).thenReturn(gridReferencePrecision)

    val repo = mock[Repository]
    val em = mock[EntityManager]
  }

  test("an existing grid square feature should just be returned") {

    // arrange
    val f = fixture
    val feature = mock[Feature]
    when(f.repo.getGridSquareFeature(f.gridRef)).thenReturn(Some((feature, mock[GridSquare])))

    // act
    val ingester = new FeatureIngester(f.em, f.repo, f.gridSquareInfoFactory)
    val result = ingester.ensureFeature(f.record)

    // assert
    result should be (feature)
  }

  //  test("a new grid square hierarchy should be created") {
}
