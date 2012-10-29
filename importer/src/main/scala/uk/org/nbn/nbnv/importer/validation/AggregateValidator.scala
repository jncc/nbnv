package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.Result

trait AggregateValidator {
  def processRecord(record: NbnRecord): Result
  def notifyComplete()
  def name: String
}
