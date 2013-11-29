package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.{GridRefDef, NbnRecord}
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import collection.mutable.ListBuffer

class Nbnv90Validator (factory: GridSquareInfoFactory) {
  val code = "NBNV-90"

  def validate(record: NbnRecord) = {

    val results = new ListBuffer[Result]

    if (record.gridReferencePrecision.isEmpty) {
      val r2 =
        new Result {
          def level = ResultLevel.ERROR
          def reference = record.key
          def message = "%s: The grid reference precision must be specified".format(code)
        }
      results.append(r2)
    }
    else {

      if (record.gridReferencePrecision.get < 100) {
        val r1 =
          new Result {
            def level = ResultLevel.WARN
            def reference = record.key
            def message = "%s: The precision is less than 100m and will be rounded up to the maximum precision of 100m".format(code)
          }

        results.append(r1)
      }

      if (record.gridReferencePrecision.get > 10000 ) {
        val r3 =
          new Result {
            def level = ResultLevel.ERROR
            def reference = record.key
            def message = "%s: The precision is lower than minimum allowed precision of 10,000m".format(code)
           }
        results.append(r3)
      }
      else  {
        val gridRef = factory.getByGridRef(GridRefDef(record.gridReferenceRaw.get,None, None))

        val targetPrecision = if (record.gridReferencePrecision.get < 100) 100 else record.gridReferencePrecision.get

        if (targetPrecision >= gridRef.gridReferencePrecision) {
          val r4 =
            new Result {
              def level = ResultLevel.DEBUG
              def reference = record.key
              def message = "%s: Validated: Precision is less than or equal to the grid referance precision".format(code)
            }
          results.append(r4)
        }
        else {
          val r5 =
            new Result {
              def level = ResultLevel.ERROR
              def reference = record.key
              def message = "%s: The precision '%s' is higher then the precision of the grid referance '%s'".format(code, record.gridReferencePrecision, record.gridReferenceRaw)
            }
          results.append(r5)
        }
      }
    }

    results.toList
  }

}
