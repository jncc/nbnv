package uk.org.nbn.nbnv.importer

import java.util
import util.Properties
import java.io.{InputStream, FileInputStream}

/// Retrieves the settings from the application's settings.properties file(s).

object Settings {

  val coreDbSettingsMap = uk.gov.nbn.data.properties.PropertiesReader.getEffectiveProperties("coreDb.properties")
  val stagingDbSettingsMap = uk.gov.nbn.data.properties.PropertiesReader.getEffectiveProperties("stagingDb.properties")

  // provide statically-bound access to settings
  // ...
  def myTestSetting = coreDbSettingsMap.getProperty("my.test.setting")

}

