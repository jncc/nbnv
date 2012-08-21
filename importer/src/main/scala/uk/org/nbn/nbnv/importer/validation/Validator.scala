package uk.org.nbn.nbnv.importer.validation

import scala.collection.JavaConversions._
import com.google.inject.Inject
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger

// todo: requirement for allowing e.g. 100 errors - presumably this needs to keep validating, but not import?
// todo: mapping between darwin and nbn terms, separate from reading values, nulls throw?
// todo: ensure possibility for parallel


class Validator @Inject()(log: Logger){

  def validate(archive: Archive) {
    log.info("Hello from the validator.")

    // (1) archive-scoped validations
    // (2) head-scoped validations

    // (3) record-scoped validations
    for (record <- archive.iteratorRaw) {

      // an example record-scoped validation
      val v = new Nbnv62Validator
      val r = v.validate(record)
      logResult(r)
    }
  }

  private def logResult(result: Result) {

    def output = "Validation: " + result.reference + " | " + result.message

    result.level match {
      case ResultLevel.INFO  => log.info(output)
      case ResultLevel.WARN  => log.warn(output)
      case ResultLevel.ERROR => log.error(output)
    }
  }
}
