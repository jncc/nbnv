package uk.org.nbn.nbnv.importer.data

import scala.collection.JavaConversions._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.{SpatialQueries, StoredProcedureLibrary}

class Repository (log: Logger, em: EntityManager, cache: QueryCache) extends ControlAbstractions {
  def confirmOrganisationExits(name: String) = {
    val q = "select count(o.id) from Organisation o where o.name = :name"

    cacheSingle(q, name) {
      val query = em.createQuery(q)
      query.setParameter("name", name)

      val count = query.getSingleResult.asInstanceOf[Long]
      if (count > 0) true else false
    }
  }

  def getSurvey(surveyKey: String, taxonDatasetKey: String) = {
    val q = "SELECT s FROM Survey s WHERE s.providerKey = :providerKey AND s.taxonDataset.datasetKey = :taxonDatasetKey"

    cacheSome(q, surveyKey, taxonDatasetKey) {

      val query = em.createQuery(q, classOf[Survey])
      query.setParameter("providerKey", surveyKey)
      query.setParameter("taxonDatasetKey", taxonDatasetKey)

      query.getSingleOrNone
    }
  }

  def setDatasetPublic(datasetKey: String) {
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.setDatasetPublic(datasetKey)
  }

  def confirmDatasetExists(datasetKey: String): Boolean = {
    val q = "select count(d.key) from Dataset d where d.key = :key"

    cacheSingle(q, datasetKey) {
      val query = em.createQuery(q)
      query.setParameter("key", datasetKey)

      val count = query.getSingleResult.asInstanceOf[Long]
      if (count > 0 ) true else false
    }
  }

  def confirmUserExistsByEamil(email: String): Boolean = {

    val q = "select count(u.id) from User u where u.email = :email"

    cacheSingle(q, email) {
      val query = em.createQuery(q)
      query.setParameter("email", email)

      val count = query.getSingleResult.asInstanceOf[Long]
      if (count > 0) true else false
    }
  }

  def getImportSiteByName(name: String, dataset: ImportDataset): Option[ImportSite] = {
    val q = "select s from ImportSite s where s.name = :name and s.datasetKey = :dataset and s.providerKey is null"

    cacheSome(q, name, dataset.getKey) {

      val query = em.createQuery(q, classOf[ImportSite])
      query.setParameter("name", name)
      query.setParameter("dataset", dataset)

      query.getSingleOrNone
    }
  }

  def clearImportStagingTables(){
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.clearImportStagingTables
  }


  def confirmSiteBoundary(siteDatasetKey: String, siteProviderKey: String): Boolean = {

    val query = em.createQuery("select count(sb.featureID) from SiteBoundary sb join SiteBoundaryDataset sbd where sbd.datasetKey = :datasetKey and sb.providerKey = :providerKey")
    query.setParameter("datasetKey", siteDatasetKey)
    query.setParameter("providerKey", siteProviderKey)

    val count = query.getSingleResult.asInstanceOf[Long]
    if (count == 1) true else false
  }


  def getSRSForLatLong(lng: Double, lat: Double ) : Option[Int] = {

    val queries = new SpatialQueries(em)
    val wkt = "POINT(%s %s)".format(lng.toString, lat.toString)

    val result = queries.getGridProjectionForWGS84wkt(wkt)

    // manually wrap in an Option because getGridProjection returns java.lang.Integer
    // which results in a null pointer exception
    if (result == null) None else Option(result)
  }

  def createFeature(wkt: String, identifier: String) = {

    val sprocs = new StoredProcedureLibrary(em)
    sprocs.createFeature(wkt, identifier)
  }

  def createGridRef(feature: Feature, gridRef: String , resolution: Resolution , projection: Projection , wkt: String ) : GridSquare = {
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.createGridSquare(gridRef, resolution, projection,wkt, feature)
  }

  def deleteTaxonObservationsAndRelatedRecords(datasetKey: String) {
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.deleteTaxonObservationsAndRelatedRecords(datasetKey)
  }

  def importTaxonObservationsAndRelatedRecords() = {
    val sprocs = new StoredProcedureLibrary(em)
    sprocs.importTaxonObservationsAndRelatedRecords
  }

  def confirmTaxonVersionKey(taxonVersionKey: String): Boolean = {
    val q = "SELECT COUNT(t.taxonVersionKey) FROM Taxon t WHERE t.taxonVersionKey = :tvk";

    cacheSingle(q, taxonVersionKey) {
      val query = em.createQuery(q, classOf[Taxon])
      query.setParameter("tvk", taxonVersionKey)

      val count = query.getSingleResult.asInstanceOf[Long]
      if (count == 1) true else false
    }
  }

  def getAttribute(attributeLabel: String, taxonDataset: ImportTaxonDataset) = {

    val q = "select a from ImportAttribute a join a.importTaxonObservationAttributeCollection toa join toa.importTaxonObservation to join to.sampleID s join s.surveyID sv where a.label = :label and sv.datasetKey = :dataset"

    cacheSome(q, attributeLabel, taxonDataset.getDatasetKey) {

      val query = em.createQuery(q, classOf[ImportAttribute])
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

  def getSiteBoundaryFeature(siteBoundaryDatasetKey: String, providerKey: String) = {

    val q = "select f, b from Feature f join f.siteBoundary b where b.siteBoundaryDataset.datasetKey = :siteBoundaryDatasetKey and b.providerKey = :providerKey "

    cacheSingle(q, siteBoundaryDatasetKey, providerKey) {

      val query = em.createQuery(q)
      query.setParameter("siteBoundaryDatasetKey", siteBoundaryDatasetKey)
      query.setParameter("providerKey", providerKey)

      expectSingleResult(siteBoundaryDatasetKey + "|" + providerKey) {
        query.getResultList map { case Array(f: Feature, b: SiteBoundary) => (f, b) }
      }
    }
  }

  def getImportSurvey(surveyKey: String, taxonDataset: ImportTaxonDataset) = {

    val q = "SELECT s FROM ImportSurvey s WHERE s.providerKey = :providerKey AND s.datasetKey = :taxonDataset"

    cacheSome(q, surveyKey, taxonDataset.getDatasetKey) {

      val query = em.createQuery(q, classOf[ImportSurvey])
      query.setParameter("providerKey", surveyKey)
      query.setParameter("taxonDataset", taxonDataset)

      query.getSingleOrNone
    }
  }

  def getFeature(id: Int) = {

    cacheSingle("getFeature", id.toString) {
      em.findSingle(classOf[Feature], id)
    }
  }

  def getTaxonDataset(key: String) = {

    cacheSingle("getTaxonDataset", key) {
      em.findSingleOrNone(classOf[TaxonDataset], key) getOrElse {
        throw new BadDataException("Dataset '%s' does not exist. Please check the key is correct.".format(key))
      }
    }
  }

  def getTaxonObservation(key: String, sample: ImportSample) = {

    val q = "select o from ImportTaxonObservation o where o.providerKey = :key and o.sampleID = :sample "

    em.createQuery(q, classOf[ImportTaxonObservation])
      .setParameter("key", key)
      .setParameter("sample", sample)
      .getSingleOrNone
  }

  def getTaxonObservationPublic(id: Int) = {

    val q = "select o from ImportTaxonObservationPublic o where o.taxonObservationID = :id "

    em.createQuery(q, classOf[ImportTaxonObservationPublic])
      .setParameter("id", id)
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

  def getFirstImportRecorder(name: String) = {

    val q = "select r from ImportRecorder r where r.name = :name "

    cacheSome("getFirstImportRecorder", name) {

      val query = em.createQuery(q, classOf[ImportRecorder])
      query.setParameter("name", name)
      query.getFirstOrNone
    }
  }

  def getImportSiteByKey(providerKey: String, dataset: ImportDataset) = {

    val q = "select s from ImportSite s where s.providerKey = :providerKey and s.datasetKey = :dataset "

    cacheSome(q, providerKey, dataset.getKey) {

      val query = em.createQuery(q, classOf[ImportSite])
      query.setParameter("providerKey", providerKey)
      query.setParameter("dataset", dataset)

      query.getSingleOrNone
    }
  }

  def getLatestDatasetKey = {

    val q = "select d.key from Dataset d where d.key like 'GA%' order by d.key desc"

    em.createQuery(q, classOf[String]).setMaxResults(1).getFirstOrNone
  }

  def getImportSample(providerKey: String, survey: ImportSurvey) = {

    val q = "SELECT s FROM ImportSample s WHERE s.providerKey=:providerKey AND s.surveyID = :survey"

    cacheSome(q, providerKey, survey.getId.toString) {

      em.createQuery(q, classOf[ImportSample])
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
        throw new BadDataException("Cache key component in '%s' was null. This could lead to incorrect data.".format(cacheKey.mkString("|")))
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
