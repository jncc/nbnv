package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.records._
import util.parsing.json.JSON
import org.apache.log4j.Logger
import com.google.inject.Inject
import java.util.{Calendar, Date}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class NbnRecordFactory @Inject()(log: Logger) {

  def makeRecord(rawData: List[String], metadata: ArchiveMetadata) : NbnRecord = {
    def getData(fieldIndex: Option[Int]) : Option[String] = {
      if (!fieldIndex.isDefined) { None }
      else if (fieldIndex.get > (rawData.length - 1)) {
        //A peculiarity of .split (used in CsvReader) means that a sequence of blank fields are
        //truncated
//        log.warn("The field index %d exceeds the number of fields in the row".format(fieldIndex.get))
        None
      }
      else {
        Option(rawData(fieldIndex.get).trim.replaceAll("^\"|\"$", "")).filter(_.nonEmpty)
      }
    }

    def parseOccurrenceStatus(s: Option[String]) = {
      s match {
        case None => false
        case Some(x) => x.toLowerCase match {
          case "presence" => false
          case "absence" => true
          case _ => {
            log.debug("Invalid Occurence status in record '%s'. Data should be rejected by validation".format(getData(metadata.key).getOrElse("<Blank Key>")))
            false
          }
        }
      }
    }

    def getAttributes(attributes: Option[String]) = {
      if (!attributes.isDefined) { Map.empty[String, String] }
      else {
        try {
          val json = JSON.parseFull(attributes.get)
          json.get.asInstanceOf[Map[String, String]]
        }
        catch {
          case e: Throwable => {
            log.debug("Improperly formed JSON attribute list in record '%s'. Data should be rejected by validation".format(getData(metadata.key).getOrElse("<Blank Key>")))
            Map.empty[String, String]
          }
        }
      }
    }

    def parseDate(dateString: Option[String], getEndDate: Boolean = false): Option[Date] = {

      dateString match {
        case Some(ds) => {

          var date : Option[Date] = None

          List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy")
            .toStream
            .takeWhile(_ => date == None)
            .foreach(df => date = ds.maybeDate(df))

          val cal = Calendar.getInstance

          if (date == None) {
            date = ds.maybeDate("MMM yyyy")
            if (date != None && getEndDate) {
              cal.setTime(date.get)
              cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
              date = Some(cal.getTime)
            }
          }

          if (date == None) {
            date = ds.maybeDate("yyyy")
            if(date != None && getEndDate) {
              cal.setTime(date.get)
              cal.add(Calendar.YEAR, 1)
              cal.set(Calendar.DAY_OF_YEAR,1)
              cal.add(Calendar.DAY_OF_WEEK, -1)

              date = Some(cal.getTime)
            }
          }

          date
        }
        case None => None
      }
    }

    def parseSensitiveOccurrence(rawOccurrence : Option[String]) : Boolean = {
      if (rawOccurrence.isDefined) {
        val value = rawOccurrence.get.maybeBoolean

        if (value.isDefined && value.get == true) {
          true
        } else {
          false
        }
      } else {
        false
      }
    }

    def getDateType() : Option[String] = {
      getData(metadata.dateType) match {
        case Some(d) => Some(d)
        case None => {
          //If event date is defined default to date type D if no other has been specified.
          if (getData(metadata.date).isDefined) Some("D")
          else None
        }
      }
    }

    new NbnRecord {
      val key = getData(metadata.key).get
      val absenceRaw = getData(metadata.absence)
      val absence = parseOccurrenceStatus(getData(metadata.absence))
      val surveyKey = getData(metadata.surveyKey)
      val sampleKey = getData(metadata.sampleKey)
      val taxonVersionKey = getData(metadata.taxonVersionKey)
      val siteKey = getData(metadata.siteKey)
      val siteName = getData(metadata.siteName)
      val recorder = getData(metadata.recorder)
      val determiner = getData(metadata.determiner)
      val eastRaw = getData(metadata.east)
      val east = getData(metadata.east) map { _.maybeDouble } getOrElse None
      val northRaw = getData(metadata.north)
      val north = getData(metadata.north) map { _.maybeDouble } getOrElse None
      val srs = getData(metadata.srs) map { _.maybeInt } getOrElse None
      val srsRaw = getData(metadata.srs)
      val attributesRaw = getData(metadata.attributes)
      val attributes = getAttributes(getData(metadata.attributes))
      val eventDateRaw = getData(metadata.date)
      val startDateRaw = getData(metadata.startDate)
      val startDate = parseDate(getData(metadata.startDate).orElse(getData(metadata.date)))
      val endDateRaw = getData(metadata.endDate)
      val endDate = parseDate(getData(metadata.endDate))
      val sensitiveOccurrenceRaw = getData(metadata.sensitiveOccurrence)
      val sensitiveOccurrence = parseSensitiveOccurrence(getData(metadata.sensitiveOccurrence))
      val gridReferenceTypeRaw = getData(metadata.gridReferenceType)
      val gridReferenceRaw = getData(metadata.gridReference)
      val gridReferencePrecision = getData(metadata.gridReferencePrecision) map { _.maybeInt } getOrElse None
      val gridReferencePrecisionRaw = getData(metadata.gridReferencePrecision)
      val featureKey = getData(metadata.featureKey)
      val dateType = getDateType
      val dateTypeRaw = getData(metadata.dateType)
    }
  }


}
