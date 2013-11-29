package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

// The start date should not be before the end date, can however be the same
class Nbnv72Validator {

  //todo: write test for this
  def validate(record: NbnRecord) = {

    if (record.startDate.isDefined && record.endDate.isDefined) {

      if (record.startDate.get.compareTo(record.endDate.get) > 0 ) {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "NBNV-72: End date is before start date"
        }
      } else {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "NBNV-72: Validated: Start and end dates are valid"
        }
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "NBNV-72: Validated: Nbnv-72 validation rule not applicable"
      }
    }
  }
}
