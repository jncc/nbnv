package uk.org.nbn.nbnv.importer

import java.util
import util.Properties
import java.io.{InputStream, FileInputStream}

/// Retrieves the settings from the application's settings.properties file(s).

object Settings {

  val map = uk.gov.nbn.data.properties.PropertiesReader.getEffectiveProperties("importer.properties")

  // provide statically-bound access to settings
  // ...
  def myTestSetting = map.getProperty("my.test.setting")
  def connectionString = map.getProperty("javax.persistence.jdbc.url")
  def datasetReset = map.getProperty("resetter.path")
}

