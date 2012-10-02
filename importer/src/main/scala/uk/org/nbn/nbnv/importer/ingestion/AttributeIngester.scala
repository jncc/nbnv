package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.jpa.nbncore._
import util.parsing.json.JSON
import uk.org.nbn.nbnv.importer.data.Repository
import scala.Some

class AttributeIngester @Inject()(log: Logger,
                                  em: EntityManager,
                                  repo: Repository){


  def ingestAttributes(record: NbnRecord, observation: TaxonObservation, dataset: TaxonDataset) {

    //clear the collection of existing attributes (this is for records that are being re-imported
    observation.getTaxonObservationAttributeCollection.clear()

    for ((attributeLabel, value) <- record.attributes) {

      val attribute = ensureAttribute(attributeLabel)

      val toa = new TaxonObservationAttribute()
      val pk = new TaxonObservationAttributePK()
      pk.setAttributeID(attribute.getId)
      pk.setObservationID(observation.getId)
      toa.setTaxonObservationAttributePK(pk)
      toa.setTextValue(value)

      em.persist(toa)
    }

    def ensureAttribute(attributeLabel: String) = {
      val attribute = repo.getAttribute(attributeLabel, dataset)

      attribute match {
        case Some(a) => a
        case None => {

          val storageLevel = em.find(classOf[AttributeStorageLevel], 4) //observation
          val storageType = em.find(classOf[AttributeStorageType], 3) //for now all attributes are free text

          val a = new Attribute()
          a.setLabel(attributeLabel)
          a.setDescription(attributeLabel)
          a.setStorageLevelID(storageLevel)
          a.setStorageTypeID(storageType)

          em.persist(a)
          em.flush()
          a
        }
      }
    }
  }
}
