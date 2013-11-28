package uk.org.nbn.nbnv.importer.ingestion

import java.util.{Calendar, Date}
import java.text._
import uk.org.nbn.nbnv.importer.BadDataException
import scala.Some
import uk.org.nbn.nbnv.importer.BadDataException

//NBNV-794 - The list of allowable date types was reduced from that shown below. The restriction is enforced by
//the Nbnv78Validator.

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


class NbnDateParser {
  def parse(dateType: String, startDate: Option[Date], endDate: Option[Date]) : (Option[Date], Option[Date]) = {
    dateType match {
      case "<D" => {
        endDate match {
          case Some(d) => (None,Some(d))
          case None => throw new BadDataException("No end date. An end date must be specified for dateType '<D'")
        }
      }
      case  ">D" => {
        startDate match {
          case Some(d) => (Some(d), None)
          case None => throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        }
      }
      case "D" => {
        startDate match {
          //Validation should ensure start date is same as end date if provided (NBNV 73)
          case Some(d) => (Some(d), Some(endDate.getOrElse(d)))
          case None => throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        }
      }
      case "<Y" | "-Y" => {

        val date = endDate.getOrElse(
          throw new BadDataException("No end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        val endOfYear = getEndOfYear(date)

        (None, Some(endOfYear))
      }
      case ">Y" | "Y-" => {
        val date = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfYear = getStartOfYear(date)

        (Some(startOfYear), None)
      }
      case "DD" => {

        val periodStart = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val periodEnd = endDate.getOrElse(
          throw new BadDataException("No end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (periodEnd.compareTo(periodStart) < 0 ) throw new BadDataException("Start date is before end dtate")

        (Some(periodStart), Some(periodEnd))
      }
      case "M" | "O" => {
        val date = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfMonth = getStartOfMonth(date)
        val endOfMonth = getEndOfMonth(date)

        (Some(startOfMonth), Some(endOfMonth))
      }
      case "MM" | "OO" | "P" => {
        val periodStart = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val periodEnd = endDate.getOrElse(
          throw new BadDataException("No end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (periodEnd.compareTo(periodStart) < 0 ) throw new BadDataException("Start date is before end dtate")

        val startOfMonth = getStartOfMonth(periodStart)
        val endOfMonth = getEndOfMonth(periodEnd)

        (Some(startOfMonth), Some(endOfMonth))
      }
      case "ND"| "U" => (None, None)
      case "Y" => {
        val date = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val startOfYear = getStartOfYear(date)
        val endOfYear = getEndOfYear(date)

        (Some(startOfYear), Some(endOfYear))
      }
      case "YY" => {
        val periodStart = startDate.getOrElse(
          throw new BadDataException("No start date. A start date must be specified for dateType '%s'".format(dateType))
        )

        val periodEnd = endDate.getOrElse(
          throw new BadDataException("No end date. An end date must be specified for dateType '%s'".format(dateType))
        )

        if (periodEnd.compareTo(periodStart) < 0 ) throw new BadDataException("Start date is before end dtate")

        val startOfYear = getStartOfYear(periodStart)
        val endOfYear = getEndOfYear(periodEnd)

        (Some(startOfYear), Some(endOfYear))
      }
      case _ => throw new BadDataException("Invalid date type '%s'".format(dateType))
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
}
