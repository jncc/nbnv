package uk.org.nbn.nbnv.importer.smoke

import scala.collection.JavaConversions._
import uk.org.nbn.nbnv.importer._
import uk.org.nbn.nbnv.importer.ingestion.FeatureIngester
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer.data.{QueryCache, Database, Repository}
import uk.org.nbn.nbnv.importer.data.Implicits._
import uk.org.nbn.nbnv.importer.Settings
import uk.org.nbn.nbnv.importer.records._
import uk.org.nbn.nbnv.PersistenceUtility
import org.apache.log4j.Logger
import org.scalatest.matchers._



class GridSquareSmokeSuite extends BaseFunSuite with ResourceLoader {

  def fixture = new {
    val qc = new QueryCache(mock[Logger])
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val db = new Database(em, new Repository(mock[Logger], em, qc), qc)
  }
  
  def queryType : Int

  test("should create entries in the importFeatures tables when creating features") {
    def f = fixture
    
    val gridSquareInfoFactory = new GridSquareInfoFactory(f.db)
    val featureIngester = new FeatureIngester(mock[Logger], f.db, gridSquareInfoFactory)
    
    val gridRefDef = new GridRefDef("OM99", Option(new SrsDef(27700)), Option(10000))
    
    val (feat) = featureIngester.ensureGridRefFeature(gridRefDef)
    
    val q = "SELECT * FROM importFeatures"

    val query = f.em.createQuery(q, classOf[Integer])
    val result = query.getResultList
    
    result should contain (feat.getId)   
    
    // Clean up
    f.db.repo.clearImportStagingTables
    
    val d = "DELETE FROM Feature WHERE id = :id"
    val queryD = f.em.createQuery(d)
    queryD.setParameter("id", feat.getId)
    query.getSingleResult
  }
  
  test("should correctly do spatial calculations for a gridsquare") {
    def f = fixture
    
    val gridSquareInfoFactory = new GridSquareInfoFactory(f.db)
    val featureIngester = new FeatureIngester(mock[Logger], f.db, gridSquareInfoFactory)
    
    val gridRefDef = new GridRefDef("OM999999", Option(new SrsDef(27700)), Option(100))
    
    val (grid100m) = featureIngester.ensureGridRefFeature(gridRefDef)
    
    var q = "SELECT * FROM importFeatures"

    var query = f.em.createQuery(q, classOf[Integer])
    var result = query.getResultList
    
    result should have size 3
       
    val grid10k = f.db.repo.getGridSquareFeature("OM99") 
    val grid2k = f.db.repo.getGridSquareFeature("OM99Z") 
    val grid1k = f.db.repo.getGridSquareFeature("OM9999")
    
    f.db.repo.calculateSiteSpatialInteractions
    
    // 100m
    
    q = "SELECT containedFeatureID FROM FeatureContains WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid100m.getId)
    result = query.getResultList
    
    result should contain (grid100m.getId)
    
    q = "SELECT overlappedFeatureID FROM FeatureOverlaps WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid100m.getId)
    result = query.getResultList
    
    result should contain (grid100m.getId)   
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result should contain (grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))

    // 1km
    
    q = "SELECT containedFeatureID FROM FeatureContains WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    
    q = "SELECT overlappedFeatureID FROM FeatureOverlaps WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)   
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result should contain (grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))    
    
    // 2km
    
    q = "SELECT containedFeatureID FROM FeatureContains WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    
    q = "SELECT overlappedFeatureID FROM FeatureOverlaps WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)   
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result should contain (grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))  

    // 10km
    
    q = "SELECT containedFeatureID FROM FeatureContains WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result should contain (grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))         
    
    q = "SELECT overlappedFeatureID FROM FeatureOverlaps WHERE id = :id"
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))
    result = query.getResultList
    
    result should contain (grid100m.getId)   
    result should contain (grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    result should contain (grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    result should contain (grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))         
    
    q = "DELETE FROM Feature WHERE id = :id"
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid100m.getId)
    query.getSingleResult
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid1k.map(_._1.getId) getOrElse (fail("No 1KM Grid Created")))
    query.getSingleResult
       
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid2k.map(_._1.getId) getOrElse (fail("No 2KM Grid Created")))
    query.getSingleResult
    
    query = f.em.createQuery(q, classOf[Integer])
    query.setParameter("id", grid10k.map(_._1.getId) getOrElse (fail("No 10KM Grid Created")))
    query.getSingleResult
  }
}