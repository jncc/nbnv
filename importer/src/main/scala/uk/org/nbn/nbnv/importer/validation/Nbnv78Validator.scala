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

  def failD(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "The Date Type : '%s' requires that the startDate and endDate are the same instead we found start: '%s' end: '%s'".format(record.dateType, record.startDateRaw, record.endDateRaw)
    }
  }

  def process(record: NbnRecord) = {
    if (record.startDate != null && record.endDate != null) {
      record.dateType match {
        case "D"  => if (record.startDate.equals(record.endDate)) success(record) else failD(record)
        case "DD" => success(record) // If we have a valid start/end date then this requires no checks
        case "O"  => processO(record)
        case "OO" => processO(record)
        case "Y"  => processY(record)
        case "YY" => processY(record)
        case _ => fail(record) // Should never happen
      }
    } else if (record.eventDate != null && record.startDate == null && record.endDate == null) {
      record.dateType match {
        case "D"  => success(record)
        case "DD" => success(record)
        case "O"  => fail(record)
        case "OO" => fail(record)
        case "Y"  => fail(record)
        case "YY" => fail(record)
        case _ => fail(record) // Should never happen
      }
    }
  }

  def processO(record:NbnRecord) = {
    null
  }

  def processY(record:NbnRecord) = {
    null
  }

  def success(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = record.dateType
      def message: String = "Found an accepted DateType and associated dates"
    }
  }
}
