package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv86Validator {
  def validate(record: NbnRecord) = {
    val longitude = record.east.get
    val latitude = record.north.get

    if (longitude > 180 || longitude < -180) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "NBNV-86: Longitude does not fall on the earth"
      }
    }
    else if (latitude > 90 || latitude < -90) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "NBNV-86: Latitiude does not fall on the earth"
      }
    }
    else{
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "NBNV-86: Validated: Nbnv86 not implemented"
      }
    }
  }
}
