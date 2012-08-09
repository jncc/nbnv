package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import data.{Repository, KeyGenerator}
import ingestion._
import uk.org.nbn.nbnv.importer.logging.Log
import uk.org.nbn.nbnv.metadata.{MetadataParser, MetadataReader}
import uk.org.nbn.nbnv.utility.FileSystem
import uk.org.nbn.nbnv.PersistenceUtility
import org.apache.log4j.{Level, Logger}

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
        sys.exit(1)
      }
    }
  }

  /// Creates an importer instance for real life use
  /// with its dependencies injected.
  def createImporter(options: Options) = {

    // configure log
    Log.configure(options.logDir, "4MB", Level.ALL)
    val log = Log.get()

    // create entity manager
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager

    // manually inject dependencies
    new Importer(options,
                 log,
                 new ArchiveManager(options),
                 new MetadataReader(new FileSystem, new MetadataParser),
                 new Ingester(em,
                              new DatasetIngester(em, new KeyGenerator(new Repository(em)), new Repository(em)),
                              new RecordIngester(log,
                                                 em,
                                                 new SurveyIngester(em),
                                                 new SampleIngester(em),
                                                 new Repository(em)
                              )))
  }
}

/// Imports data into the NBN Gateway core database.
class Importer(options:        Options,
               log:            Logger,
               archiveManager: ArchiveManager,
               metadataReader: MetadataReader,
               ingester:       Ingester) {

  def run() {

    withTopLevelExceptionHandling {

      log.info("Welcome! Starting the NBN Gateway importer...")
      log.info("Options are: \n" + options)

      // open the archive and read the metadata
      val archive = archiveManager.open()
      val metadata = metadataReader.read(archive)

      // validate
      // ...
      // verify

      // ingest the archive and metadata
      ingester.ingest(archive, metadata)

      log.info("Done importing archive '%s'.".format(options.archivePath))
    }
  }

  private def withTopLevelExceptionHandling(f: => Unit) {
    try {
      f
    }
    catch {
      case e: Exception => {
        log.fatal("Unhandled exception!", e)
        throw e
      }
    }
  }
}
