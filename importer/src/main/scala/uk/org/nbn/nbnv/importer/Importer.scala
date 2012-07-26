package uk.org.nbn.nbnv.importer

import logging.Log
import uk.org.nbn.nbnv.metadata.{MetadataParser, MetadataReader}
import uk.org.nbn.nbnv.utility.FileSystem
import java.io.File
import org.gbif.dwc.text.ArchiveFactory
import org.apache.log4j.{Level, Logger}

object Importer {

  def main(args: Array[String]) {
    Options.parse(args.toList) match {
      case OptionsSuccess(options) => {
        createImporter(options).run()
      }
      case OptionsFailure(message) => {
        println(message)
        exit(1)
      }
    }
  }

  def createImporter(options: Options) : Importer = {

    Log.configure(options.logDir, "4MB", Level.ALL)
    val log = Log.getLog

    // todo use guice
    new Importer(options, log, new MetadataReader(new FileSystem, new MetadataParser))
  }

}

class Importer(options: Options, log: Logger, metadataReader: MetadataReader) {
  def run() {
    log.info("Welcome! Starting the NBN Gateway importer...")
    log.info("Options are: ... todo")

    //val archive = ArchiveFactory.openArchive(new File(options.archivePath), new File(options.tempDir))
    // read EML metadata
    //val metadata = metadataReader.read(archive)
//    open dwca reader
//    begin tx
//      upsert dataset, samples and surveys
//    loop through the records
//      get survey for this dataset using key
//      create it if not exists
//      get sample for this dataset using key
//      create it if not exists
//      upsert observation
//      end tx

  }
}


//    val em = createEntityManager()
//    em.getTransaction.begin()
//
//    val archive = ArchiveFactory.openArchive(new File("c:\\working\\uk-dwca.zip"), new File("c:\\working\\deleteme"))
//
//    //    val metadataReader = new MetadataReader()
//    //    val metadata = metadataReader.read(archive)
//
//    for (record <- archive.iteratorRaw) {
//      println("upserting record " + record.core.value(DwcTerm.occurrenceID))
//      // in our case we know there should be exactly one extension record ("head" is first in a list)
//      val extensionRecord = record.extension("http://uknbn.org/terms/NBNExchange").head
//      upsertRecord(em, record, extensionRecord)
//    }
//    //em.getTransaction.commit

//  def upsertRecord(em: EntityManager, r: StarRecord, er: Record) {
//
//    val format = new SimpleDateFormat("dd/MM/yyyy")
//    val dateType = em.find(classOf[DateType], er.value("http://uknbn.org/terms/dateType"))
//    val site = em.find(classOf[Site], 1)
//    val sample = em.find(classOf[Sample], 1)
//    val feature = em.find(classOf[Feature], 1)
//    val taxon = em.find(classOf[Taxon], r.core.value(DwcTerm.taxonID))
//    val determiner = em.find(classOf[Recorder], 1)
//    val recorder = em.find(classOf[Recorder], 1)
//    val o = new TaxonObservation()
//    o.setAbsenceRecord(false)
//    o.setDateEnd(format.parse(r.core.value(DwcTerm.eventDate))) //wrong
//    o.setDateStart(format.parse(r.core.value(DwcTerm.eventDate)))
//    o.setDateType(dateType)
//    o.setDeterminerID(determiner)
//    o.setFeatureID(feature)
//    //o.setObservationID()
//    o.setObservationKey(r.core.value(DwcTerm.occurrenceID))
//    o.setRecorderID(recorder)
//    o.setSampleID(sample)
//    o.setSensitiveRecord(false)
//    o.setSiteID(site)
//    o.setTaxonVersionKey(taxon)
//    em.merge(o)
//  }
//
//  def createEntityManager() = {
//    val u = new PersistenceUtility()
//    val f = u.createEntityManagerFactory()
//    f.createEntityManager()
//  }
