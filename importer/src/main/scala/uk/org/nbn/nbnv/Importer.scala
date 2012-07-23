
package uk.org.nbn.nbnv

import org.gbif.dwc.text.{UnsupportedArchiveException, StarRecord, ArchiveFactory}
import org.gbif.dwc.terms.{DwcTerm, ConceptTerm}
import scala.collection.JavaConversions._
import jpa.nbncore._
import java.io.File
import javax.persistence.EntityManager
import org.gbif.dwc.text.ArchiveFactory
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc
import dwc.record.Record
import java.text.SimpleDateFormat


object Importer {

  val datasetKey = "TestDWCReader"
  val datasetTitle = "JUnit Test dataset arg"

  def main(args: Array[String]) {
    val em = createEntityManager()
    em.getTransaction.begin()

    val archive = ArchiveFactory.openArchive(new File("c:\\working\\uk-dwca.zip"), new File("c:\\working\\deleteme"))
    
    val metadataReader = new MetadataReader()
    val metadata = metadataReader.GetMetaData(archive.getMetadataLocationFile)
    
    for (record <- archive.iteratorRaw) {
      println("upserting record " + record.core.value(DwcTerm.occurrenceID))
      // in our case we know there should be exactly one extension record ("head" is first in a list)
      val extensionRecord = record.extension("http://uknbn.org/terms/NBNExchange").head
      upsertRecord(em, record, extensionRecord)
    }
    em.getTransaction.commit
  }

  def upsertRecord(em: EntityManager, r: StarRecord, er: Record) {
    
    val format = new SimpleDateFormat("dd/MM/yyyy")
    val dateType = em.find(classOf[DateType], er.value("http://uknbn.org/terms/dateType"))
    val site = em.find(classOf[Site], 1)
    val sample = em.find(classOf[Sample], 1)
    val feature = em.find(classOf[Feature], 1)
    val taxon = em.find(classOf[Taxon], r.core.value(DwcTerm.taxonID))
    val determiner = em.find(classOf[Recorder], 1)
    val recorder = em.find(classOf[Recorder], 1)
    val o = new TaxonObservation()
    o.setAbsenceRecord(false)
    o.setDateEnd(format.parse(r.core.value(DwcTerm.eventDate))) //wrong
    o.setDateStart(format.parse(r.core.value(DwcTerm.eventDate)))
    o.setDateType(dateType)
    o.setDeterminerID(determiner)
    o.setFeatureID(feature)
    //o.setObservationID()
    o.setObservationKey(r.core.value(DwcTerm.occurrenceID))
    o.setRecorderID(recorder)
    o.setSampleID(sample)
    o.setSensitiveRecord(false)
    o.setSiteID(site)
    o.setTaxonVersionKey(taxon)
    em.merge(o)
  }

  def createEntityManager() = {
    val u = new PersistenceUtility()
    val f = u.createEntityManagerFactory()
    f.createEntityManager()
  }
}
