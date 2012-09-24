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

//  test("a new grid square hierarchy should be created") {
  test("an existing grid square feature should be returned without ceremony") {

    // arrange

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

    val em = mock[EntityManager]

    val feature = mock[Feature]
    val gridSquare = mock[GridSquare]

    val repo = mock[Repository]
    when(repo.getGridSquareFeature(gridRef)).thenReturn(Some((feature, gridSquare)))

    // act
    val ingester = new FeatureIngester(em, repo, gridSquareInfoFactory)
    val result = ingester.ensureFeature(record)

    // assert
    result should be (feature)
//    // assert - that the entity manager was not called with the retrieved dataset
//    verify(em, never()).persist(dataset)
//    // assert - that a property was set
//    result.getDatasetKey should be (metadata.datasetKey)
  }

}
