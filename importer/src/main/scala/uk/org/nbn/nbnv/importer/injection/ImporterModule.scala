package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.importer.Options
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level

/// The guice configuration module for the importer.
class ImporterModule(options: Options) extends AbstractModule {

  Log.configure(options.logDir, "4MB", Level.ALL)

  override def configure() {}

  @Provides
  def getOptions = options

  @Provides
  def getLog = Log.get()
}
