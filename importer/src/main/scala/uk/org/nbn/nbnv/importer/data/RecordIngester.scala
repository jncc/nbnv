/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
;


class RecordIngester (log: Logger, em: EntityManager, surveyIngester : SurveyIngester, sampleIngester : SampleIngester) {
  
  def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

    log.info("Upserting record %s".format(record.key))

    //todo: Generate surveyKey if blank
    val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    
    //todo: Generate sampleKey if blank
    val sample = sampleIngester.upsertSample(record.sampleKey, survey)

    val site = em.find(classOf[Site], record.siteKey)
    //todo : do something if the site can't be found

    
  }
}
