package uk.org.nbn.nbnv.importer

import testing.BaseFunSuite

class SettingsSuite extends BaseFunSuite {

  test("should be able to read a property from coreDb.properties") {

    Settings.myCoreDbTestSetting should be ("don't-delete-this")
  }

  test("should be able to read a property from stagingDb.properties") {

    Settings.myStagingDbTestSetting should be ("don't-delete-this")
  }
}
