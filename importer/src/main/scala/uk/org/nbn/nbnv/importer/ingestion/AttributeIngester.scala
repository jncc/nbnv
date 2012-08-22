package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.jpa.nbncore.TaxonObservation
import util.parsing.json.JSON

class AttributeIngester @Inject()(log: Logger,
                                  em: EntityManager){
  def ingestAttributes(record: NbnRecord, observation: TaxonObservation ) {
    //todo : check what is returned from the record if there are no attributes
    if (record.attributes == "") return;

    val json = JSON.parseFull(record.attributes)
    val attributes = json.get.asInstanceOf[Map[String, Any]]

    attributes foreach {case(label, value) => println("attribute: " + label + " --> " + value) }
  }
}
