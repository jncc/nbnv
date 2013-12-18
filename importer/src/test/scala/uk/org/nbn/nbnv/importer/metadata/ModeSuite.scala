package uk.org.nbn.nbnv.importer.metadata

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class ModeSuite extends BaseFunSuite {

  test("should parse upsert") {
    val r = Mode.stringToMaybeValue("upsert")

    r should be (Some(Mode.upsert))
  }

  test("should parse append") {
    val r = Mode.stringToMaybeValue("append")

    r should be (Some(Mode.append))
  }

  test("should return none for anything else") {
    val r = Mode.stringToMaybeValue("wibble")

    r should be (None)
  }

  test("should parse mixed case ") {
    val r = Mode.stringToMaybeValue("apPeNd")

    r should be (Some(Mode.append))
  }

}
