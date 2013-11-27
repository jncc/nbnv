package uk.org.nbn.nbnv.importer.records

import scala.collection.JavaConversions._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import uk.org.nbn.nbnv.importer.BadDataException
import util.parsing.json.JSON
import java.util.{Calendar, Date}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

/// Wraps a Darwin record in NBN clothing.
class NbnRecord(record: StarRecord) {

  // TODO: THIS CLASS NEEDS TO BE SPLIT INTO AT LEAST TWO; ONE FOR VALIDATION AND ONE FOR INGESTION

  // there should be exactly one extension record for a record (hence .head)
  private val extension = record.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence").head

  private val attributeJson = record.core.value(DwcTerm.dynamicProperties)
  private val attributeMap = if (attributeJson != null && attributeJson.isEmpty == false) {
      try {
        val json = JSON.parseFull(attributeJson)
        json.get.asInstanceOf[Map[String, String]]
      }
      catch {
        case e: Throwable => {
          throw new BadDataException("Improperly formed JSON attribute list in record '%s'".format(this.key))
        }
      }
    }
    else {
      Map.empty[String, String]
    }

  def key =                record.core.value(DwcTerm.occurrenceID)
  def absenceRaw =         parseOptional(record.core.value(DwcTerm.occurrenceStatus))
  def absence =            absenceRaw map { parseOccurrenceStatus(_) } getOrElse false
  def surveyKey =          parseOptional(record.core.value(DwcTerm.collectionCode))
  def sampleKey =          parseOptional(record.core.value(DwcTerm.eventID))
  def taxonVersionKey =    parseOptional(record.core.value(DwcTerm.taxonID)) getOrElse ""
  def siteKey =            parseOptional(record.core.value(DwcTerm.locationID))
  def siteName =           parseOptional(record.core.value(DwcTerm.locality))
  def recorder =           parseOptional(record.core.value(DwcTerm.recordedBy))
  def determiner =         parseOptional(record.core.value(DwcTerm.identifiedBy))
  def eastRaw =            parseOptional(record.core.value(DwcTerm.verbatimLongitude))
  def east =               parseOptional(record.core.value(DwcTerm.verbatimLongitude)) map { s => s.toDouble }
  def northRaw =           parseOptional(record.core.value(DwcTerm.verbatimLatitude))
  def north =              parseOptional(record.core.value(DwcTerm.verbatimLatitude)) map { s => s.toDouble }
  def srs =                parseOptional(record.core.value(DwcTerm.verbatimSRS)) map { s => s.toInt }
  def srsRaw =             parseOptional(record.core.value(DwcTerm.verbatimSRS))
  def attributes =         attributeMap

  def eventDateRaw      = parseOptional(record.core.value(DwcTerm.eventDate))
  def startDateRaw      = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart"))
  def startDate         = parseDate(startDateRaw.orElse(eventDateRaw))
  def endDateRaw        = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd"))
  def endDate           = parseDate(endDateRaw,true)

  def sensitiveOccurrenceRaw    = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence"))
  def sensitiveOccurrence       = sensitiveOccurrenceRaw map { _.toBoolean } getOrElse false

  def gridReferenceTypeRaw      = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType"))
  def gridReferenceRaw          = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference"))
  def gridReferencePrecision    = gridReferencePrecisionRaw map { _.toInt }
  def gridReferencePrecisionRaw = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecision"))

  def featureKey             = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/siteFeatureKey"))

  /// Returns Some(value) if the string is not null or empty, otherwise None.
  def parseOptional(value: String) = Option(value).filter(_.trim.nonEmpty)

  def parseOccurrenceStatus(s: String) = s.toLowerCase match {
    case "presence" => false
    case "absence" => true
    case _ => throw new BadDataException("Invalid occurrence status '%s'".format(s))
  }

  def dateType  = {
    if (eventDateRaw.isDefined) "D"
    else extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")
  }

  //getEndDate parses MM yyyy and yyyy to end of year / month rather then start as default
  private def parseDate(dateString: Option[String], getEndDate: Boolean = false): Option[Date] = {

    dateString match {
      case Some(ds) => {

        var date : Option[Date] = None

        List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy")
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

  def feature = {
    if (gridReferenceRaw.isDefined)
      GridRefDef(gridReferenceRaw.get, parseSpatialSystem, gridReferencePrecision)
    else if (featureKey.isDefined)
      BoundaryDef(featureKey.get)
    else if (east.isDefined && north.isDefined && (srs.isDefined || gridReferenceTypeRaw.isDefined))
      PointDef(east.get, north.get, parseSpatialSystem.get, gridReferencePrecision)
    else
      throw new BadDataException("Couldn't parse feature.")
  }

  def parseSpatialSystem = {
    if (gridReferenceTypeRaw.isDefined)
      Some(GridTypeDef(gridReferenceTypeRaw.get))
    else if (srs.isDefined)
      Some(SrsDef(srs.get))
    else
      None
  }
}


