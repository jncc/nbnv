
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import org.apache.log4j.Logger
;

class SurveyIngester @Inject()(db: Database, log: Logger) {

  def stageSurvey(surveyKey: Option[String], dataset: ImportTaxonDataset) {

    val key = surveyKey getOrElse "1" // if no survey key provided

    if (!db.repo.getImportSurvey(key, dataset).isDefined) {
      log.info("Upserting survey %s for dataset %s".format(key, dataset.getDatasetKey))

      val s = new ImportSurvey()
      s.setProviderKey(key)
      s.setDatasetKey(dataset)

      //read in existing metadata if any
      db.repo.getSurvey(key, dataset.getDatasetKey) match {
        case Some(os) => {
          s.setTitle(os.getTitle)
          s.setDescription(os.getDescription)
          s.setGeographicalCoverage(os.getGeographicalCoverage)
          s.setTemporalCoverage(os.getTemporalCoverage)
          s.setDataCaptureMethod(os.getDataCaptureMethod)
          s.setPurpose(os.getPurpose)
          s.setDataQuality(os.getDataQuality)
          s.setAdditionalInformation(os.getAdditionalInformation)
        }
        case None => None
      }

      db.em.persist(s)
    }
  }
}
