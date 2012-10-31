package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import collection.mutable.ListBuffer

class Nbnv90Validator (factory: GridSquareInfoFactory) {
  def validate(record: NbnRecord) = {

    val results = new ListBuffer[Result]

    if (record.gridReferencePrecision < 100) {
      val r1 =
        new Result {
          def level = ResultLevel.WARN
          def reference = record.key
          def message = "NBNV-90: The target precision is less than 100m and will be rounded up to the maximum precision of 100m"
        }

      results.append(r1)
    }

    if (record.gridReferencePrecision == 0) {
      val r2 =
        new Result {
        def level = ResultLevel.DEBUG
        def reference = record.key
        def message = "NBNV-90: Validated: No target precision specified"
      }
      results.append(r2)
    }
    else if (record.gridReferencePrecision > 10000 ) {
      val r3 =
        new Result {
          def level = ResultLevel.ERROR
          def reference = record.key
          def message = "NBNV-90: Target precision is lower than minimum allowed precision of 10,000m"
         }
      results.append(r3)
    }
    else {
      val gridRef = factory.getByGridRef(record.gridReference.get)

      val targetPrecision = if (record.gridReferencePrecision < 100) 100 else record.gridReferencePrecision

      if (targetPrecision >= gridRef.gridReferencePrecision) {
        val r4 =
          new Result {
            def level = ResultLevel.DEBUG
            def reference = record.key
            def message = "NBNV-90: Validated: Precision is less than or equal to the grid referance precision"
          }
        results.append(r4)
      }
      else {
        val r5 =
          new Result {
            def level = ResultLevel.ERROR
            def reference = record.key
            def message = "NBNV-90: The precision '%s' is higher then the precision of the grid referance '%s'".format(record.gridReferencePrecision, record.gridReference)
          }
        results.append(r5)
      }
    }

    results.toList
  }

}
