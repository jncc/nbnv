package uk.org.nbn.nbnv.importer

import java.util
import util.Properties
import java.io.{InputStream, FileInputStream}

/// Retrieves the settings from the application's settings.properties file(s).

object Settings {

  val map = new Properties

  withInputStream(this.getClass.getResourceAsStream("/settings/settings.properties")) {
    s => map.load(s)
  }

  // override with server-specific file on a conventional path
  private val overridingFile = new java.io.File("/settings/settings.properties")

  if (overridingFile.exists) {
    val overriding = new Properties

    withInputStream(new FileInputStream(overridingFile)) {
      s => overriding.load(s)
    }
    map.putAll(overriding)
  }

  // provide statically-bound access to settings
  // ...
  def myTestSetting = map.getProperty("my.test.setting")

  def withInputStream(s: InputStream)(f: InputStream => Unit) {
    try {
      f(s)
    }
    finally {
      s.close()
    }
  }
}

