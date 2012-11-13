package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial.{UnknownGrid, GridSystem}


//validate if a recognised SRS or grid ref type is supplied.
class Nbnv185Validator {
  def validate(record: NbnRecord) = {
    if (record.srsRaw.isDefined) {
      record.srsRaw match {
        case Some("27700") | Some("29903") | Some("23030") | Some("4326") =>
          new Result {
            def level = ResultLevel.DEBUG
            def message = "NBNV-185: Validated: A recognised SRS was provided"
            def reference = record.key
          }
         case _ =>
            new Result {
              def level = ResultLevel.ERROR
              def message = "NBNV-185: Unrecognised SRS '%s'".format(record.srsRaw.get)
              def reference = record.key
            }
        }
    }
    else if (record.gridReferenceTypeRaw.isDefined){
      GridSystem(record.gridReferenceTypeRaw.get) match {
        case UnknownGrid =>
          new Result {
            def level = ResultLevel.ERROR
            def message = "NBNV-185: Unrecognised grid reference type '%s'".format(record.gridReferenceTypeRaw.get)
            def reference = record.key
          }
        case _ =>
          new Result {
            def level = ResultLevel.DEBUG
            def message = "NBNV-185: Validated: A recognised grid reference type was provided"
            def reference = record.key
          }
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-185: The spatial reference system or grid system type must be specified"
        def reference = record.key
      }
    }
  }
}
