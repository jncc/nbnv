package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv78Validator {

  // Record DateType must be one of the following
  def validate(record: NbnRecord) = record.dateType match {
    case "D" | "DD" | "O" | "OO" | "Y" | "YY" => success(record)  // todo: Needs to continue processing the data to ensure the dates provided do actually fit this description
    case _ => fail(record)
  }

  def fail(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "The Date Type : " + record.dateType + " is not a supported DateType => \"D\",\"DD\",\"O\",\"OO\",\"Y\",\"YY\""
    }
  }

  def success(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = record.dateType
      def message: String = "Found a valid date type"
    }
  }
}
