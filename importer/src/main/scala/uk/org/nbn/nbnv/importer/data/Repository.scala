package uk.org.nbn.nbnv.importer.data

import scala.collection.JavaConversions._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject

class Repository @Inject()(em: EntityManager) extends ControlAbstractions {
  def confirmTaxonVersionKey(taxonVersionKey: String): Boolean = {
    val query = em.createQuery("SELECT t FROM Taxon t WHERE t.taxonVersionKey = :tvk", classOf[Taxon])
    query.setParameter("tvk", taxonVersionKey)
    val tvkCount = query.getResultList.size

    if (tvkCount == 1) true else false

  }


  def getAttribute(attributeLabel: String, taxonDataset: TaxonDataset) = {

    val query = em.createQuery("select a from Attribute a join a.taxonObservationAttributeCollection toa join toa.taxonObservation to join to.sampleID s join s.surveyID sv where a.label = :label and sv.datasetKey = :dataset", classOf[Attribute])
    query.setParameter("label", attributeLabel)
    query.setParameter("dataset", taxonDataset)
    query.getFirstOrNone
  }

  def getSurvey(surveyKey: String, dataset: TaxonDataset) = {

    val query = em.createQuery("SELECT s FROM Survey s WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
    query.setParameter("surveyKey", surveyKey)
    query.setParameter("datasetKey", dataset)
    query.getSingleOrNone
  }

  def getDateType(t: String) = em.findSingle(classOf[DateType], t)

  def getFeature(id: Int) = em.findSingle(classOf[Feature], id)

  def getTaxon(key: String) = em.findSingle(classOf[Taxon], key)

  def getTaxonDataset(key: String) = em.findSingle(classOf[TaxonDataset], key)

  def getTaxonObservation(key: String, sample: Sample): Option[TaxonObservation] = {

    val q = "select o from TaxonObservation o where o.observationKey = :key and o.sampleID = :sample "

    em.createQuery(q, classOf[TaxonObservation])
      .setParameter("key", key)
      .setParameter("sample", sample)
      .getSingleOrNone
  }

  def getOrganisation(name: String): Organisation = {

    val q = "select o from Organisation o where o.organisationName = :name "

    val query = em.createQuery(q, classOf[Organisation])

    expectSingleResult(name) {
      query.setParameter("name", name).getResultList
    }
  }

  def getFirstRecorder(name: String) = {

    val q = "select r from Recorder r where r.recorderName = :name "

    val query = em.createQuery(q, classOf[Recorder])
    query.setParameter("name", name).getFirstOrNone
  }

  def getSite(siteKey: String, dataset: Dataset) = {

    val q = "select s from Site s where s.siteKey = :siteKey and s.datasetKey = :dataset "

    val query = em.createQuery(q, classOf[Site])
    query.setParameter("siteKey", siteKey)
    query.setParameter("dataset", dataset)
    query.getSingleOrNone
  }

  def getLatestDatasetKey = {

    val q = "select d.datasetKey from Dataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    em.createQuery(q, classOf[String]).setMaxResults(1).getFirstOrNone
  }

  def getSample(key: String, survey: Survey) = {
    em.createQuery("SELECT s FROM Sample s WHERE s.sampleKey=:sampleKey AND s.surveyID = :surveyID", classOf[Sample])
      .setParameter("sampleKey", key)
      .setParameter("surveyID", survey)
      .getSingleOrNone
  }
}
