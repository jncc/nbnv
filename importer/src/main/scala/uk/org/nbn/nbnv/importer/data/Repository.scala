package uk.org.nbn.nbnv.importer.data

import scala.collection.JavaConversions._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.{SpatialQueries, FeatureFactory}


class Repository @Inject()(log: Logger, em: EntityManager, cache: QueryCache) extends ControlAbstractions {

  def getSRSForLatLong(lat: Double, lng: Double) = {
    val queries = new SpatialQueries(em)
    val wkt = "POINT(%s %s)".format(lng.toString, lat.toString)

    queries.getGridProjectionForWGS84wkt(wkt)
  }

  def createFeature(wkt: String) = {

    val factory = new FeatureFactory(em)
    factory.createFeature(wkt);
  }

  def createGridRef(feature: Feature, gridRef: String , resolution: Resolution , projection: Projection , wkt: String ) : GridSquare = {
    val factory = new FeatureFactory(em)
    factory.createGridSquare(gridRef, resolution, projection,wkt, feature)
  }


  def confirmTaxonVersionKey(taxonVersionKey: String): Boolean = {
    val query = em.createQuery("SELECT t FROM Taxon t WHERE t.taxonVersionKey = :tvk", classOf[Taxon])
    query.setParameter("tvk", taxonVersionKey)

    query.getResultList.size == 1
  }

  def getAttribute(attributeLabel: String, taxonDataset: TaxonDataset) = {

    val q = "select a from Attribute a join a.taxonObservationAttributeCollection toa join toa.taxonObservation to join to.sample s join s.survey sv where a.label = :label and sv.taxonDataset = :dataset"

    cacheSome(q, attributeLabel, taxonDataset.getDatasetKey) {

      val query = em.createQuery(q, classOf[Attribute])
      query.setParameter("label", attributeLabel)
      query.setParameter("dataset", taxonDataset)
      query.getFirstOrNone
    }
  }

  def getGridSquareFeature(gridRef: String): Option[(Feature, GridSquare)] = {

    val q = "select f, s from Feature f join f.gridSquareCollection s where s.gridRef = :gridRef"

    cacheSome(q, gridRef) {

      val query = em.createQuery(q)
      query.setParameter("gridRef", gridRef)
      query.getSingleOrNone collect { case Array(f: Feature, s: GridSquare) => (f, s) }
    }
  }

  def getGridSquareFeature(featureID: Int): Option[(Feature, GridSquare)] = {

    val q = "select f, s from Feature f join f.gridSquareCollection s where f.id = :featureID"

    cacheSome(q, featureID.toString) {

      val query = em.createQuery(q)
      query.setParameter("featureID", featureID)
      query.getSingleOrNone collect { case Array(f: Feature, s: GridSquare) => (f, s) }
    }
  }

  def getSiteBoundaryFeature(siteBoundaryDataset: String, providerKey: String) = {

    val q = "select f, b from Feature f join f.siteBoundary b where b.siteBoundaryDataset = :siteBoundaryDataset and b.providerKey = :providerKey "

    cacheSingle(q, siteBoundaryDataset, providerKey) {

      val query = em.createQuery(q)
      query.setParameter("siteBoundaryDataset", siteBoundaryDataset)
      query.setParameter("providerKey", providerKey)

      expectSingleResult(siteBoundaryDataset + providerKey) {
        query.getResultList map { case Array(f: Feature, b: SiteBoundary) => (f, b) }
      }
    }
  }

  def getSurvey(surveyKey: String, taxonDataset: TaxonDataset) = {

    val q = "SELECT s FROM Survey s WHERE s.providerKey = :providerKey AND s.taxonDataset = :taxonDataset"

    cacheSome(q, surveyKey, taxonDataset.getDatasetKey) {

      val query = em.createQuery(q, classOf[Survey])
      query.setParameter("providerKey", surveyKey)
      query.setParameter("taxonDataset", taxonDataset)

      query.getSingleOrNone
    }
  }

  def getDateType(key: String) = {

    cacheSingle("getDateType", key) {
      em.findSingle(classOf[DateType], key)
    }
  }

  def getFeature(id: Int) = {

    cacheSingle("getFeature", id.toString) {
      em.findSingle(classOf[Feature], id)
    }
  }

  def getTaxon(key: String) = {

    cacheSingle("getTaxon", key) {
      em.findSingle(classOf[Taxon], key)
    }
  }

  def getTaxonDataset(key: String) = {

    cacheSingle("getTaxonDataset", key) {
      em.findSingle(classOf[TaxonDataset], key)
    }
  }

  def getTaxonObservation(key: String, sample: Sample) = {

    val q = "select o from TaxonObservation o where o.providerKey = :key and o.sample = :sample "

    em.createQuery(q, classOf[TaxonObservation])
      .setParameter("key", key)
      .setParameter("sample", sample)
      .getSingleOrNone
  }

  def getTaxonObservationPublic(observationId: Int) = {

    val q = "select o from TaxonObservationPublic o where o.taxonObservationID = :observationId "

    em.createQuery(q, classOf[TaxonObservationPublic])
      .setParameter("observationId", observationId)
      .getSingleOrNone
  }

  def getOrganisation(name: String) = {

    val q = "select o from Organisation o where o.name = :name "

    cacheSingle(q, name) {

      val query = em.createQuery(q, classOf[Organisation])
      query.setParameter("name", name)

      expectSingleResult(name) { query.getResultList }
    }
  }

  def getProjection(label: String) = {

    val q = "select p from Projection p where p.label = :label "

    cacheSingle(q, label) {

      val query = em.createQuery(q, classOf[Projection])
      query.setParameter("label", label)

      expectSingleResult(label) { query.getResultList }
    }
  }

  def getResolution(accuracy: Int) = {

    val q = "select r from Resolution r where r.accuracy = :accuracy "

    cacheSingle(q, accuracy.toString) {

      val query = em.createQuery(q, classOf[Resolution])
      query.setParameter("accuracy", accuracy)

      expectSingleResult(accuracy) { query.getResultList }
    }
  }

  def getFirstRecorder(name: String) = {

    val q = "select r from Recorder r where r.name = :name "

    cacheSome("getFirstRecorder", name) {

      val query = em.createQuery(q, classOf[Recorder])
      query.setParameter("name", name)
      query.getFirstOrNone
    }
  }

  def getSite(providerKey: String, dataset: Dataset) = {

    val q = "select s from Site s where s.providerKey = :providerKey and s.dataset = :dataset "

    cacheSome(q, providerKey, dataset.getKey) {

      val query = em.createQuery(q, classOf[Site])
      query.setParameter("providerKey", providerKey)
      query.setParameter("dataset", dataset)

      query.getSingleOrNone
    }
  }

  def getLatestDatasetKey = {

    val q = "select d.key from Dataset d where d.key like 'GA%' order by d.key desc"

    em.createQuery(q, classOf[String]).setMaxResults(1).getFirstOrNone
  }

  def getSample(providerKey: String, survey: Survey) = {

    val q = "SELECT s FROM Sample s WHERE s.providerKey=:providerKey AND s.survey = :survey"

    cacheSome(q, providerKey, survey.getId.toString) {

      em.createQuery(q, classOf[Sample])
        .setParameter("providerKey", providerKey)
        .setParameter("survey", survey)
        .getSingleOrNone
    }
  }

  /// Caches the result of the function f - but only if that result is Some(t).
  // (this is important because we don't want to cache a None when we want to
  // later re-execute the query)
  private def cacheSome[T](cacheKey: String*)(f: => Option[T]): Option[T] = {

    for (item <- cacheKey) {
      if (item == null)
        throw new ImportFailedException("Cache key component was null. This could lead to incorrectness so we're failing.")
    }

    val key = cacheKey.map(_.trim).mkString("|")
    log.debug("Query cache key is '%s'".format(key))

    cache.get(key) orElse {
      log.debug("Query cache miss for '%s'".format(key))
      // using the map function here means we only cache the results of
      // successful queries (that return Some(t)) - we don't want
      // to cache the results of unsuccessful queries; we want to go back
      // to the db next time
      f map { t =>
        cache.put(key, t)
        t
      }
    }
  }

  /// Caches the result of the function f.
  // A bit nasty - cacheSome and cacheSingle are separate functions
  // because scala won't let us overload on f's type
  private def cacheSingle[T](cacheKey: String*)(f: => T) = {
    cacheSome(cacheKey:_*) { Some(f) }.get
  }

}
