package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.{ParseException, SimpleDateFormat}
import java.util.Date
import uk.org.nbn.nbnv.importer.ImportFailedException

class Nbnv68Validator {
  val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

  // Record Date must be of a valid format
  def validate(dateString: String): Date = {
    if (dateString != null && !dateString.trim.isEmpty) {
      for (fmt <- validFormats) {
        try {
          val sdf = new SimpleDateFormat(fmt)
          sdf.setLenient(false)
          return sdf.parse(dateString)
        } catch {
          case e: ParseException => // Do Nothing
        }
      }
      // Could not construct a valid date from the input date string
      throw new ImportFailedException("Could not determine a correct date format for the value: '%s'".format(dateString))
    } else {
      null // Return null if we have no date string (represents no data)
    }
  }
}
