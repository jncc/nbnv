package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.jpa.nbncore._
import util.parsing.json.JSON
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import scala.Some

class AttributeIngester @Inject()(log: Logger, db: Database){


  def ingestAttributes(record: NbnRecord, observation: ImportTaxonObservation, dataset: ImportTaxonDataset) {

    log.debug("Ingesting attributes...")

    // clear the collection of existing attributes (this is for records that are being re-imported)
    // todo: will this really do anything? reimported datasets will be deleted first, so the auto-gen'd PKs will be new
    observation.getImportTaxonObservationAttributeCollection.clear()

    for ((attributeLabel, value) <- record.attributes) {

      val attribute = ensureAttribute(attributeLabel)

      val toa = new ImportTaxonObservationAttribute()
      val pk = new ImportTaxonObservationAttributePK()
      pk.setAttributeID(attribute.getId)
      pk.setObservationID(observation.getId)
      toa.setImportTaxonObservationAttributePK(pk)
      toa.setTextValue(value)

      db.em.persist(toa)
    }


    def ensureAttribute(attributeLabel: String) = {

      db.repo.getAttribute(attributeLabel, dataset) getOrElse {

        val storageLevel = db.em.find(classOf[AttributeStorageLevel], 4) // observation
        val storageType = db.em.find(classOf[AttributeStorageType], 3) // for now all attributes are free text

        val a = new ImportAttribute
        a.setLabel(attributeLabel)
        a.setDescription(attributeLabel)
        a.setStorageLevelID(storageLevel.getId)
        a.setStorageTypeID(storageType.getId)
        db.em.persist(a)
        db.em.flush()
        a
      }
    }

  }
}
