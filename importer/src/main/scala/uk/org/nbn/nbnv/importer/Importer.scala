package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import logging.Log
import records.NbnRecord
import uk.org.nbn.nbnv.metadata.{MetadataParser, MetadataReader}
import uk.org.nbn.nbnv.utility.FileSystem
import org.apache.log4j.{Level, Logger}
import utility.ImportException
import javax.persistence.EntityManager;
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.data.{DatasetIngester, RecordIngester}
import scala.collection.JavaConversions._


object Importer {

  /// The entry point to the console application.
  def main(args: Array[String]) {

    Options.parse(args.toList) match {
      case OptionsSuccess(options) => {
        val importer = createImporter(options)
        importer.run()
      }
      case OptionsFailure(message) => {
        println(message)
        exit(1)
      }
    }
  }

  /// Creates an importer instance for real life use
  /// with its dependencies injected.
  def createImporter(options: Options) : Importer = {

    // configure log
    Log.configure(options.logDir, "4MB", Level.ALL)
    val log = Log.get()

    // create entity manager
    val entityManager = new PersistenceUtility().createEntityManagerFactory.createEntityManager

    // todo use guice
    new Importer(options, 
                 log, 
                 new ArchiveManager(options), 
                 new MetadataReader(new FileSystem, new MetadataParser),
                 entityManager,
                 new DatasetIngester(entityManager),
                 new RecordIngester(log, entityManager))
  }
}

/// Imports data into the NBN Gateway core database.
class Importer(options:         Options,
               log:             Logger,
               archiveManager:  ArchiveManager,
               metadataReader:  MetadataReader,
               entityManager:   EntityManager,
               datasetIngester: DatasetIngester,
               recordIngester:  RecordIngester) {
  def run() {

    log.info("Welcome! Starting the NBN Gateway importer...")
    log.info("Options are: \n" + options)

    try {
      // open the archive and read the metadata
      val archive = archiveManager.open()
      val metadata = metadataReader.read(archive)

      // begin transaction
      entityManager.getTransaction.begin()

      // upsert dataset
      val dataset = datasetIngester.upsertDataset(metadata)

      // upsert records
      for (record <- archive.iteratorRaw.map(r => new NbnRecord(r))) {
        recordIngester.upsertRecord(record, dataset)
      }

      // commit the transaction
      entityManager.getTransaction.commit()
      entityManager.close()
      
      //    open dwca reader
      //    begin tx
      //      upsert dataset
      //    loop through the records
      //      get survey for this dataset using key
      //      create it if not exists
      //      get sample for this dataset using key
      //      create it if not exists
      //      upsert observation
      //      end tx
    }
    catch {
      //Do we need to rollback the jpa transaction here 
      //or is that taken care of?
      case ie: ImportException => {
        log.error("Import run failed.", ie)
      }
      case e: Exception => {
        log.fatal("Unhandled exception!", e)
        throw e
      }
    }
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
