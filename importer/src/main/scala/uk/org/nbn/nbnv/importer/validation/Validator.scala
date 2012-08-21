package uk.org.nbn.nbnv.importer.validation

import com.google.inject.Inject
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.fidelity.{ResultLevel, Result}
import org.apache.log4j.Logger

class Validator @Inject()(log: Logger){

  def validate(archive: Archive) {
    log.info("Hello from the validator.")

    val v = new Nbnv62Validator
    val r = v.validate(archive)

    logResult(r)
  }

  private def logResult(result: Result) {

    def output = "Validation result: " + result.reference + " | " + result.message

    result.level match {
      case ResultLevel.INFO  => log.info(output)
      case ResultLevel.WARN  => log.warn(output)
      case ResultLevel.ERROR => log.error(output)
    }
  }
}
