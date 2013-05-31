//todo : Fix build of FakePersistenceTrackingEntityManager to make this work
//package uk.org.nbn.nbnv.importer.ingestion
//
//import uk.org.nbn.nbnv.importer.testing.{FakePersistenceTrackingEntityManager, BaseFunSuite}
//import org.mockito.Mockito._
//import org.mockito.Matchers._
//import javax.persistence.EntityManager
//import uk.org.nbn.nbnv.importer.data.{QueryCache, Database, Repository}
//import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
//import uk.org.nbn.nbnv.importer.records.{GridTypeDef, GridRefDef, NbnRecord}
//import uk.org.nbn.nbnv.jpa.nbncore.{ImportGridSquare, ImportFeature, GridSquare, Feature}
//import org.apache.log4j.Logger
//
//class FeatureIngesterSuite extends BaseFunSuite {
//
//  def fixture = new {
//    val gridRef = "ABCDEF"
//    val gridReferenceType = "OSGBTEST"
//    val gridReferencePrecision = 12345
//
//    val gridSquareInfo = mock[GridSquareInfo]
//    when(gridSquareInfo.gridReference).thenReturn(gridRef)
//    val gridSquareInfoFactory = mock[GridSquareInfoFactory]
//    when(gridSquareInfoFactory.getByGridRef(GridRefDef(gridRef, Some(GridTypeDef(gridReferenceType)), Some(gridReferencePrecision))))
//      .thenReturn(gridSquareInfo)
//
//    val record = mock[NbnRecord]
//    when(record.gridReferenceRaw).thenReturn(Some(gridRef))
//    when(record.gridReferenceTypeRaw).thenReturn(Some(gridReferenceType))
//    when(record.gridReferencePrecision).thenReturn(Some(gridReferencePrecision))
//
//    val log = mock[Logger]
//
//    val repo = mock[Repository]
//    val em = mock[EntityManager]
//    val db = new Database(em, repo, mock[QueryCache])
//  }
//
//  ignore("an existing grid square feature should just be returned") {
//
//    // arrange
//    val f = fixture
//    val feature = mock[ImportFeature]
//    when(f.db.repo.getGridSquareFeature(f.gridRef)).thenReturn(Some((feature, mock[ImportGridSquare])))
//
//    // act
//    val ingester = new FeatureIngester(f.log, f.db, f.gridSquareInfoFactory)
//    val result = ingester.ensureGridSquareFeature(f.record)
//
//    // assert
//    result should be (feature)
//  }
//
//  // this test was broken by the switch to using a sproc
//  ignore("a new grid square feature that should have a parent should persist a grid square hierarchy") {
//
//    // arrange
//    val f = fixture
//    when(f.db.repo.getGridSquareFeature(anyString)).thenReturn(None)
//    val parentInfo = mock[GridSquareInfo]
//    val grandparentInfo = mock[GridSquareInfo]
//    when(parentInfo.gridReference).thenReturn("PARENT")
//    when(grandparentInfo.gridReference).thenReturn("GRANDPARENT")
//    when(f.gridSquareInfo.getParentGridSquareInfo).thenReturn(Some(parentInfo))
//    when(parentInfo.getParentGridSquareInfo).thenReturn(Some(grandparentInfo))
//    when(grandparentInfo.getParentGridSquareInfo).thenReturn(None)
//
//    val em = new FakePersistenceTrackingEntityManager
//
//    // act
//    val ingester = new FeatureIngester(f.log, f.db, f.gridSquareInfoFactory)
//    ingester.ensureGridSquareFeature(f.record)
//
//    // assert
//    val persistedGridSquares = em.buffer collect { case gs: GridSquare => gs.getGridRef }
//    persistedGridSquares should equal (List("GRANDPARENT", "PARENT", "ABCDEF"))
//  }
//
//  // this test was broken by the switch to using a sproc
//  ignore("a new grid square feature that shouldn't have a parent shouldn't persist a grid square hierarchy") {
//
//    // arrange
//    val f = fixture
//    when(f.repo.getGridSquareFeature(anyString)).thenReturn(None)
//    when(f.gridSquareInfo.getParentGridSquareInfo).thenReturn(None)
//
//    // act
//    val ingester = new FeatureIngester(f.log, f.db, f.gridSquareInfoFactory)
//    ingester.ensureGridSquareFeature(f.record)
//
//    // assert - that exactly one GridSquare should be persisted
//    verify(f.em, times(1)).persist(isA(classOf[GridSquare]))
//  }
//
//}
//
