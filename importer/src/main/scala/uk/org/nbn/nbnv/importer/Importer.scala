package uk.org.nbn.nbnv.importer

import darwin.ArchiveManager
import ingestion._
import injection.ImporterModule
import uk.org.nbn.nbnv.metadata.MetadataReader
import org.apache.log4j.Logger
import com.google.inject.{Inject, Guice}
import utility.Stopwatch
import validation.Validator

object Importer {

  /// The entry point to the console application.
  def main(args: Array[String]) : Unit = {

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

    val injector = Guice.createInjector(new ImporterModule(options))
    injector.getInstance(classOf[Importer])
  }
}

/// Imports data into the NBN Gateway core database.
class Importer @Inject()(options:        Options,
                         log:            Logger,
                         stopwatch:      Stopwatch,
                         archiveManager: ArchiveManager,
                         metadataReader: MetadataReader,
                         validator:      Validator,
                         ingester:       Ingester) {

  def run() {

    withTopLevelExceptionHandling {

      log.info("Welcome! Starting the NBN Gateway importer")
      log.info("Options are: \n" + options)

      val stopwatch = new Stopwatch().start()

      // open the archive and read the metadata
      val archive = archiveManager.open()
      val metadata = metadataReader.read(archive)

      // validate the archive
      validator.validate(archive)
      // verify (... or ideally in the same parallel step as validate)

      log.info("Finished validation in " + stopwatch.elapsedSeconds + " seconds")

      // ingest the archive and metadata
      ingester.ingest(archive, metadata)

      log.info("Finished ingestion in " + stopwatch.elapsedSeconds + " seconds")
      log.info("Done with archive '%s'".format(options.archivePath))
    }
  }

  private def withTopLevelExceptionHandling(f: => Unit) {
    try { f }
    catch {
      case ie: ImportFailedException => {
        log.error("Import run failed", ie)
        throw ie
      }
      case e: Throwable => {
        log.fatal("Unhandled exception!", e)
        throw e
      }
    }
  }
}
