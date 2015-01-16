package uk.org.nbn.nbnv.importer

import archive.Archive
import ingestion._
import injection.ImporterModule
import injection.DaemonImporterModule
import uk.org.nbn.nbnv.importer.metadata.MetadataReader
import org.apache.log4j.Logger
import com.google.inject.{Inject, Guice}
import utility.Stopwatch
import validation.Validator
import javax.validation.ConstraintViolationException
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
        sys.exit(1)
      }
    }
  }
  
  def startImporterFromDaemon(archive: String, target: String, logLevel: String, logDir:String, tempDir:String, logger: Logger) = {   
    val options = Options(archivePath = archive, target = createTargetFromString(target), logLevel = logLevel, logDir = logDir, tempDir = tempDir)
    
    val injector = Guice.createInjector(new DaemonImporterModule(options, logger))
    val importer = injector.getInstance(classOf[Importer])
    
    importer.daemonRun()
  }
  
  def createTargetFromString(target: String) : Target.Value = {
    if (target == "validate")
      return Target.validate
    if (target == "verify") {
      return Target.verify
    }
    if (target == "ingest") {
      return Target.ingest
    }
    if (target == "commit") {
      return Target.commit
    }
    
    return Target.validate
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
                         archive:        Archive,
                         metadataReader: MetadataReader,
                         validator:      Validator,
                         ingester:       Ingester) {

  def run() {
    withTopLevelExceptionHandling {
      importerRun(options, log, stopwatch, archive, metadataReader, validator, ingester)
    }
  }
  
  private def importerRun(options:        Options,
                         log:            Logger,
                         stopwatch:      Stopwatch,
                         archive:        Archive,
                         metadataReader: MetadataReader,
                         validator:      Validator,
                         ingester:       Ingester) {
      log.info("Welcome! Starting the NBN Gateway importer")
      log.info("Options are: \n" + options + "\n\n")

      log.info("JDBC Databse Connection: %s".format(Settings.connectionString))

      val stopwatch = new Stopwatch().start()

      // open the archive and read the metadata
      archive.open(options.archivePath)
      val metadata = metadataReader.read(archive.getArchiveFiles.metadata)

      // validate
      if (options.target >= Target.validate) {
        validator.validate(archive, metadata)
        log.info("Finished validation in " + stopwatch.elapsedSeconds + " seconds")
      }

      // verify
      if (options.target >= Target.verify) {
        // (... ideally in the same parallel step as validate)
      }

      // ingest
      if (options.target >= Target.ingest) {
        ingester.ingest(archive, metadata)
      }

      log.info("Finished import run in " + stopwatch.elapsedSeconds + " seconds")
      log.info("Done with archive '%s'".format(options.archivePath))        
  }  
  
  private def withTopLevelExceptionHandling(f: => Unit) {
    try { f }
    catch {
      case e: BadDataException => {
        log.error("Import run failed", e)
        throw e
      }
      case e: ConstraintViolationException => {
        log.fatal("Unhandled exception!", e)
        for (v <- e.getConstraintViolations) {
          log.info(v.getPropertyPath + v.getMessage)
        }
        throw e
      }
      case e: Throwable => {
        log.fatal("Unhandled exception!", e)
        throw e
      }
    }
  }
  
  def daemonRun () {
    withTopLevelExceptionHandlingDaemon {
      importerRun(options, log, stopwatch, archive, metadataReader, validator, ingester)
    }
  }
  
  private def withTopLevelExceptionHandlingDaemon(f: => Unit) {
    try { f }
    catch {
      case e: BadDataException => {
        log.info("Import run failed", e)
        throw e
      }
      case e: ConstraintViolationException => {
        log.fatal("Unhandled exception!", e)
        for (v <- e.getConstraintViolations) {
          log.info(v.getPropertyPath + v.getMessage)
        }
        throw e
      }
      case e: Throwable => {
        log.fatal("Unhandled exception!", e)
        throw e
      }
    }    
  }
}
