
package uk.org.nbn.nbnv

import jpa.nbncore._
import javax.persistence.EntityManager
import io.Source

object Importer {

  val datasetKey = "TESTPETE"
  val datasetTitle = "JUnit Test dataset"
  val dataPath = "C:\\Work\\nbnv-example-dataset\\TESTDS01.txt"

  def main(args: Array[String]) {
    val em = createEntityManager()
    em.getTransaction.begin()
    for (line <- Source.fromFile(dataPath).getLines().drop(1).take(10)) {
      // parse the record from the text line
      val record = new RecordParser().parse(line)
      println("parsed record " + record.recordKey)
      // stick the record in the database
      upsertRecord(em, record)
      println("upserted record " + record.recordKey)
    }
    //em.getTransaction.commit
  }

  def upsertRecord(em: EntityManager, r: Record) {
    val dateType = em.find(classOf[DateType], r.dateType)
    val site = em.find(classOf[Site], 1)
    val sample = em.find(classOf[Sample], 1)
    val feature = em.find(classOf[Feature], 1)
    val taxon = em.find(classOf[Taxon], r.taxonVersionKey)
    val determiner = em.find(classOf[Recorder], 1)
    val recorder = em.find(classOf[Recorder], 1)
    val o = new TaxonObservation()
    o.setAbsenceRecord(false)
    o.setDateEnd(r.endDate)
    o.setDateStart(r.startDate)
    o.setDateType(dateType)
    o.setDeterminerID(determiner)
    o.setFeatureID(feature)
    //o.setObservationID()
    o.setObservationKey(r.recordKey)
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
