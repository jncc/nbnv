package uk.org.nbn.nbnv.importer.data

import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.data.Implicits._

class Repository(em: EntityManager) extends ControlAbstractions {

  def getSurvey(surveyKey: String, dataset: TaxonDataset) = {

    val query = em.createQuery("SELECT s FROM Survey s WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
    query.setParameter("surveyKey", surveyKey)
    query.setParameter("datasetKey", dataset)
    query.getSingleOrNone
  }


  def getDateType(t: String) = em.findSingle(classOf[DateType], t)

  def getFeature(id: Int) = em.findSingle(classOf[Feature], id)

  def getTaxon(key: String) = em.findSingle(classOf[Taxon], key)

  def getOrganisation(name: String) = {

    val q = "select o from Organisation o where o.organisationName = :name "

    val query = em.createQuery(q, classOf[Organisation])
    query.setParameter("name", name).getSingleOrNone
  }

  def getFirstRecorder(name: String) = {

    val q = "select r from Recorder r where r.recorderName = :name "

    val query = em.createQuery(q, classOf[Recorder])
    query.setParameter("name", name).getFirstOrNone
  }

  def getSite(siteKey: String) = {

    val q = "select s from Site s where s.siteKey = :siteKey "

    val query = em.createQuery(q, classOf[Site])
    query.setParameter("siteKey", siteKey).getSingleResult
  }

  def getLatestTaxonDatasetKey = {

    val q = "select d.datasetKey from TaxonDataset d " +
      "where d.datasetKey like 'GA%' " +
      "order by d.datasetKey desc"

    em.createQuery(q, classOf[String]).setMaxResults(1).getFirstOrNone
  }
}
