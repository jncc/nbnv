package uk.org.nbn.nbnv.importer.data

import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.spatial._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.ingestion._
import uk.org.nbn.nbnv.importer.records._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.StoredProcedureLibrary
import org.eclipse.persistence.internal.jpa.EntityManagerImpl
import org.eclipse.persistence.sessions.Session;

class RepoSuite extends BaseFunSuite {
  def fixture = new {    
    val em = mock[EntityManagerImpl] 
    val log = mock[Logger]
    val queryCache = mock[QueryCache]
    
    when(em.getActiveSession()).thenReturn(mock[Session])
    //when(em.find(GridSquare, gridRef)).thenReturn(new GridSquare())
    
    val repo = new Repository(log, em, queryCache)
    
    //val sprocs = spy(new StoredProcedureLibrary(em))
    
  }
//    val gridRef = "OM99"
//    val resolution = 10000
//    
//    val projectionObj = new Projection(1, 1, "27700")
//    val resolutionObj = new Resolution(1, "10km")
//
//    val gridSquareInfo = new BritishGridSquareInfo(gridRef, Option(resolution))
//    val gridRefDef = new GridRefDef("OM99", Option(new SrsDef(27700)), Option(resolution))
//    
//    val feature = new Feature(1)
//
//    val repo = new Repository(mock[Logger], mock[EntityManager],  mock[QueryCache])
//    
//    when(repo.getProjection("27700")).thenReturn(projectionObj)
//    when(repo.getResolution(10000)).thenReturn(resolutionObj)
//    when(repo.createFeature(gridSquareInfo.wgs84polygon, gridSquareInfo.gridReference)).thenReturn(feature)
//
//    val gridSquareInfoFactory = mock[GridSquareInfoFactory]
//    
//
//    
//    when(gridSquareInfoFactory.getByGridRef(gridRefDef)).thenReturn(gridSquareInfo)
//    
//    val database = new Database(mock[EntityManager], repo, mock[QueryCache])
//    val featureIngester = new FeatureIngester(mock[Logger], database, gridSquareInfoFactory)    
//    
//    val record = mock[NbnRecord]
//
//    
//    when(record.feature).thenReturn(gridRefDef)
//    when(repo.getGridSquareFeature(gridRef)).thenReturn(null)
  
  ignore("Should call function to persist features in import features table when creating features") {
    val f = fixture
    
    val feature = new Feature(1)
    val gridRef = "OM99"
    val resolution = new Resolution(1, "10km")
    val projection = new Projection(1)
    val wkt = "WKT"    
    val importFeature = new ImportFeatures(feature.getId);
    
    f.repo.createGridRef(feature, gridRef, resolution, projection, wkt)
        
//    verify(f.sprocs.createGridSquare(gridRef, resolution, projection, wkt, feature))
//    verify(f.sprocs.createFeature(wkt, gridRef))
    
    verify(f.em.persist(importFeature))
    
//    val f = fixture
//    
//    f.featureIngester.ensureFeature(f.record)    
//    
//    verify(f.featureIngester.ensureGridRefFeature(f.gridRefDef))
//    verify(f.repo.getGridSquareFeature(f.gridRef))
//       
//    verify(f.repo.createGridRef(f.feature, f.gridSquareInfo.gridReference, f.resolutionObj, f.projectionObj, f.gridSquareInfo.sourceProjectionPolygon))
//    

  }
  
  ignore("Should call sproc to perform spatial intersection calculations over features in import features table after import") {
    
  }
}
