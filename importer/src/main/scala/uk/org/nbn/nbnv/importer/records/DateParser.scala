package uk.org.nbn.nbnv.importer.records

import java.util.{Calendar, Date}
import java.text._
import uk.org.nbn.nbnv.importer.ImportFailedException
import scala.Some
import uk.org.nbn.nbnv.importer.ImportFailedException

//<D	Before Date – end date only
//<Y	Before Year- end date only
//>D	After Date – start date only
//>Y	After Year – start date only
//B	Unknown – fail – not supported
//C	Circa – fail – not supported
//D	Day – start date or both but they must be the same
//DD	Day Range – both – must be full date
//M	Month – start or both – must be the same month and must me the start and end date of that month
//MM	Month Range – both – can be the same month
//ND	No date - neither
//O	Month = M
//OO	Month Range = MM
//P	Publication Date – fail – not supported at this stage of the import. Handled in the ui
//R	Unknown - fail
//U	= ND
//XX	Unknown- fail
//Y	Year – start or both start to end of year must be the same year – start and end date of year
//-Y	Before Year – end date only
//Y-	After Year – start date only
//YY	Year Range – both- can be the same year


class DateParser {
  def parse(dateType: String, startDateString: String, endDateString: String) : (Option[Date], Option[Date]) = {
    dateType match {
      case "<D" => {
        parseDate(endDateString) match {
          case Some(d) => (None,Some(d))
          case None => throw new ImportFailedException("Invalid end date. An end date must be specified for dateType '<D'")
        }
      }
      case ">D" => {
        parseDate(startDateString) match {
          case Some(d) => (Some(d), None)
          case None => throw new ImportFailedException("Invalid start date. A start date must be specified for dateType 'D>'")
        }
      }
      case "<Y" | "-Y" => {

        val endDate = parseDate(endDateString).getOrElse(
          throw new ImportFailedException("Invalid end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        val endOfYear = getEndOfYear(endDate)

        (None, Some(endOfYear))
      }
      case ">Y" | "Y-" => {
        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfYear = getStartOfYear(startDate)

        (Some(startOfYear), None)
      }
      case "D" => {

        parseDate(startDateString) match {
          case Some(d) => (Some(d), Some(d))
          case None => throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        }
      }
      case "DD" => {

        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val endDate = parseDate(endDateString).getOrElse(
          throw new ImportFailedException("Invalid end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (endDate.compareTo(startDate) < 0 ) throw new ImportFailedException("Start date is before end dtate")

        (Some(startDate), Some(endDate))
      }
      case "M" | "O" => {
        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfMonth = getStartOfMonth(startDate)
        val endOfMonth = getEndOfMonth(startDate)

        (Some(startOfMonth), Some(endOfMonth))
      }
      case "MM" | "OO" => {
        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val endDate = parseDate(endDateString).getOrElse(
          throw new ImportFailedException("Invalid end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (endDate.compareTo(startDate) < 0 ) throw new ImportFailedException("Start date is before end dtate")

        val startOfMonth = getStartOfMonth(startDate)
        val endOfMonth = getEndOfMonth(endDate)

        (Some(startOfMonth), Some(endOfMonth))
      }
      case "ND"| "U" => (None, None)
      case "Y" => {
        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfYear = getStartOfYear(startDate)
        val endOfYear = getEndOfYear(startDate)

        (Some(startOfYear), Some(endOfYear))
      }
      case "YY" => {
        val startDate = parseDate(startDateString).getOrElse(
          throw new ImportFailedException("Invalid start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val endDate = parseDate(endDateString).getOrElse(
          throw new ImportFailedException("Invalid end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (endDate.compareTo(startDate) < 0 ) throw new ImportFailedException("Start date is before end dtate")

        val startOfYear = getStartOfYear(startDate)
        val endOfYear = getEndOfYear(endDate)

        (Some(startOfYear), Some(endOfYear))
      }
    }
  }

  private def getStartOfMonth(date: Date) = {
    val startOfMonth = Calendar.getInstance()
    startOfMonth.setTime(date)
    startOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    startOfMonth.getTime
  }

  private def getEndOfMonth(date: Date) = {
    val endOfMonth = Calendar.getInstance()
    endOfMonth.setTime(date)
    endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
    endOfMonth.getTime
  }

  private def getStartOfYear(date: Date) = {
    val startOfYear = Calendar.getInstance()
    startOfYear.setTime(date)
    startOfYear.set(Calendar.DAY_OF_YEAR, 1)
    startOfYear.getTime
  }

  private def getEndOfYear(date: Date) = {
    val endOfYear = Calendar.getInstance()
    endOfYear.setTime(date)
    endOfYear.set(Calendar.DAY_OF_YEAR, endOfYear.getActualMaximum(Calendar.DAY_OF_YEAR))
    endOfYear.getTime
  }

  // Record Date must be of a valid format
  private def parseDate(dateString: String): Option[Date] = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    for (fmt <- validFormats) {
      try {
        val sdf = new SimpleDateFormat(fmt)
        sdf.setLenient(false)
        return Some(sdf.parse(dateString))

      } catch {
        case e: ParseException => //move on and try the next format
      }
    }

    //No date found
    None
  }

}
