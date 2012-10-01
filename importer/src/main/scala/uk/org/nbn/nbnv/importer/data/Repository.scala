package uk.org.nbn.nbnv.importer.data

import scala.collection.JavaConversions._
import javax.persistence.{TypedQuery, EntityManager}
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.ImportFailedException

class Repository @Inject()(log: Logger, em: EntityManager, cache: QueryCache) extends ControlAbstractions {

  // todo: wot's this for? and does it need caching?
  def confirmTaxonVersionKey(taxonVersionKey: String): Boolean = {
    val query = em.createQuery("SELECT t FROM Taxon t WHERE t.taxonVersionKey = :tvk", classOf[Taxon])
    query.setParameter("tvk", taxonVersionKey)

    query.getResultList.size == 1
  }

  def getAttribute(attributeLabel: String, taxonDataset: TaxonDataset) = {

    val q = "select a from Attribute a join a.taxonObservationAttributeCollection toa join toa.taxonObservation to join to.sampleID s join s.surveyID sv where a.label = :label and sv.datasetKey = :dataset";

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

  def getSurvey(surveyKey: String, dataset: TaxonDataset) = {

    val q = "SELECT s FROM Survey s WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey"

    cacheSome(q, surveyKey, dataset.getDatasetKey) {

      val query = em.createQuery(q, classOf[Survey])
      query.setParameter("surveyKey", surveyKey)
      query.setParameter("datasetKey", dataset)

      query.getSingleOrNone
    }
  }

  def getDateType(t: String) = {

    cacheSingle("getDateType", t) {
      em.findSingle(classOf[DateType], t)
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

    val q = "select o from TaxonObservation o where o.observationKey = :key and o.sampleID = :sample "

    em.createQuery(q, classOf[TaxonObservation])
      .setParameter("key", key)
      .setParameter("sample", sample)
      .getSingleOrNone
  }

  def getTaxonObservationPublic(observationId: Int) = {

    val q = "select o from TaxonObservationPublic o where o.observationID = :observationId "

    em.createQuery(q, classOf[TaxonObservationPublic])
      .setParameter("observationId", observationId)
      .getSingleOrNone
  }

  def getOrganisation(name: String) = {

    val q = "select o from Organisation o where o.organisationName = :name "

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

    val q = "select s from Site s where s.providerKey = :providerKey and s.datasetKey = :dataset "

    cacheSome(q, providerKey, dataset.getKey) {

      val query = em.createQuery(q, classOf[Site])
      query.setParameter("providerKey", providerKey)
      query.setParameter("dataset", dataset)

      query.getSingleOrNone
    }
  }

  def getLatestDatasetKey = {

    val q = "select d.datasetKey from Dataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    em.createQuery(q, classOf[String]).setMaxResults(1).getFirstOrNone
  }

  def getSample(key: String, survey: Survey) = {

    val q = "SELECT s FROM Sample s WHERE s.providerKey=:providerKey AND s.surveyID = :surveyID"

    cacheSome(q, key, survey.getId.toString) {

      em.createQuery(q, classOf[Sample])
        .setParameter("providerKey", key)
        .setParameter("surveyID", survey)
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
