package uk.org.nbn.nbnv.importer

import testing.BaseFunSuite

class SettingsSuite extends BaseFunSuite {

  test("should be able to read a property from settings.properties") {

    Settings.myTestSetting should be ("don't-delete-this")
  }

  test("should be able to read apiUrl from a settings property")
  {
    Settings.apiUrl should be ("http://localhost:8084/api")
  }

}
