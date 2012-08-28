package uk.org.nbn.nbnv.importer.fidelity

import uk.org.nbn.nbnv.importer.fidelity.ResultLevel._

/**
* Represents a result of a validation or verification check.
*/
abstract class Result {
  def level: ResultLevel
  def reference: String
  def message : String
}
