package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

// The start date should not be before the end date, can however be the same
class Nbnv71Validator {

  def validate(record: NbnRecord) = {
    if (record.startDate != null && record.endDate != null) {
      // If our
      if (record.startDate.after(record.endDate)) {
        fail(record)
      } else {
        success(record)
      }
    } else if(record.eventDate != null) {
      success()
    } else {
      fail
    }
  }

  def fail = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = "EventDate"
      def message: String = "Could not find a valid Event Date field"
    }
  }

  def fail(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.startDateRaw
      def message: String = "The indicated start date for the record is after the end date: '%s' is before '%s'".format(record.startDateRaw, record.endDateRaw)
    }
  }

  def success() = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = "EventDate"
      def message: String = "The record has an event date and no start / end date"
    }
  }

  def success(record:NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = record.startDateRaw
      def message: String = "The indicated start date is before or the same as the indicated end date"
    }
  }
}
