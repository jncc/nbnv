package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial.{UnknownGrid, GridSystem}


//validate if a recognised SRS or grid ref type is supplied.
class Nbnv185Validator {
  val code = "NBNV-185"

  def validate(record: NbnRecord) = {
    if (record.srsRaw.isDefined) {
      record.srsRaw match {
        case Some("27700") | Some("29903") | Some("23030") | Some("4326") =>
          new Result {
            def level = ResultLevel.DEBUG
            def message = "%s: Validated: A recognised SRS was provided".format(code)
            def reference = record.key
          }
         case _ =>
            new Result {
              def level = ResultLevel.ERROR
              def message = "%s: The Projection, %s is not one of the recognised types used in the NBN Exchange Format".format(record.srsRaw.get).format(code)
              def reference = record.key
            }
        }
    }
    else if (record.gridReferenceTypeRaw.isDefined){
      GridSystem(record.gridReferenceTypeRaw.get) match {
        case UnknownGrid =>
          new Result {
            def level = ResultLevel.ERROR
            def message = "%s: The Projection, %s is not one of the recognised types used in the NBN Exchange Format".format(code, record.gridReferenceTypeRaw.get)
            def reference = record.key
          }
        case _ =>
          new Result {
            def level = ResultLevel.DEBUG
            def message = "%s: Validated: A recognised grid reference type was provided".format(code)
            def reference = record.key
          }
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The Projection is required for this record".format(code)
        def reference = record.key
      }
    }
  }
}
