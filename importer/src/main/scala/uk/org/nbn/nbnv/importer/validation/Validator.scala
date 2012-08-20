package uk.org.nbn.nbnv.importer.validation

import com.google.inject.Inject
import java.util.logging.Logger
import org.gbif.dwc.text.Archive

class Validator @Inject()(log: Logger){

  def validate(archive: Archive) {
    log.info("Hello from the validator.")
  }
}
