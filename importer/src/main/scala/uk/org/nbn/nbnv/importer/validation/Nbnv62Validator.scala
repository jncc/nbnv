package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.fidelity.{Result, ResultLevel}

import org.gbif.dwc.text.Archive
//import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv62Validator {

  def validate(archive: Archive) = {

    new Result {
      def level = ResultLevel.INFO
      def message = "Hey, something interesting happened."
      def reference = "Dataset and record #."
    }
  }
}
