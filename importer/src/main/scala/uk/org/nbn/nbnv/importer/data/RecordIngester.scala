/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.{EntityManager, Query}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
;


class RecordIngester (log: Logger, em: EntityManager) {
  
  def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

    log.info("Upserting record %s".format(record.key))

    /*Query query = entityManager.createQuery("Select sm from SecureMessage sm where sm.someField=:arg1");
     query.setParameter("arg1", arg1);*/

    val surveyQuery = em.createQuery("SELECT s FROM Survey WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
                      .setParameter("surveyKey", record.surveyKey)
                      .setParameter("datasetKey", dataset.getDatasetKey)
                      .getResultList

    val sampleQuery = em.createQuery("SELECT s FROM Sample WHERE s.sampleKey=:sampleKey AND s.surveyKey = :surveyKey", classOf[Sample])
                      .setParameter("sampleKey", record.sampleKey)
                      .setParameter("surveyKey", record.surveyKey)
                      .getResultList

    //todo: Generate new sample key if none given

    val sample = if (!sampleQuery.isEmpty) sampleQuery.get(0) else new Sample(sampleKey)
    
    val sample = if (!sampleQuery.isEmpty) sampleQuery.get(0) else getNewSample(record.sampleKey)
    
  }
}
