package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class FieldMappingValidator {
  def validate(code: String, fieldName: String, value : Option[Int]) = {
    if (value.isDefined) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = fieldName
        def message: String = "%s: The field '%s' has been mapped".format(code, fieldName)
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = fieldName
        def message: String = "%s: The field '%s' has not been mapped".format(code, fieldName)
      }
    }
  }
}
