package uk.org.nbn.nbnv.importer.records

import scala.collection.JavaConversions._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.ImportFailedException
import util.parsing.json.JSON

/// Wraps a Darwin record in NBN clothing.
class NbnRecord(record: StarRecord) {

  // there should be exactly one extension record for a record (hence .head)
  private val extension = record.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence").head

  // todo: we don't want to do parsing in this class
  // todo: .value (below) returns null if the column does not exist - should throw a better exception here
  // todo: parse all the gubbins eventDate types that NBN uses

  private val format = new SimpleDateFormat("yyyy/MM/dd")

  private val attributeJson = record.core.value(DwcTerm.dynamicProperties)
  private val attributeMap = if (attributeJson != null && attributeJson.isEmpty == false) {
      try {
        val json = JSON.parseFull(attributeJson)
        json.get.asInstanceOf[Map[String, String]]
      }
      catch {
        case e: Throwable => {
          //Return some usful error for the log.
          throw new ImportFailedException("Improperly formed JSON attribute list in record '%s'".format(this.key))
        }
      }
    }
    else {
      Map.empty[String, String]
    }

  def key =             record.core.value(DwcTerm.occurrenceID)
  def absenceRaw =     record.core.value(DwcTerm.occurrenceStatus)
  def absence =         parseOccurrenceStatus(absenceRaw)
  def surveyKey =       record.core.value(DwcTerm.collectionCode)
  def sampleKey =       record.core.value(DwcTerm.eventID)
  def taxonVersionKey = record.core.value(DwcTerm.taxonID)
  def siteKey =         record.core.value(DwcTerm.locationID)
  def siteName =        record.core.value(DwcTerm.locality)
  def recorder =        record.core.value(DwcTerm.recordedBy)
  def determiner =      record.core.value(DwcTerm.identifiedBy)
  def eventDateRaw =    record.core.value(DwcTerm.eventDate)
  def eventDate =       format.parse(eventDateRaw)
  def east =            parseOptional(record.core.value(DwcTerm.verbatimLongitude)) map { s => s.toDouble.toInt }
  def north =           parseOptional(record.core.value(DwcTerm.verbatimLatitude)) map { s => s.toDouble.toInt }
  def srs =             parseOptional(record.core.value(DwcTerm.verbatimSRS))
  def attributes =      attributeMap

  def startDateRaw           = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")
  def startDate              = format.parse(startDateRaw)
  def endDateRaw             = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")
  def endDate                = format.parse(endDateRaw)
  def dateType               = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")
  def sensitiveOccurrenceRaw = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")
  def sensitiveOccurrence    = parseSensitiveOccurrence(sensitiveOccurrenceRaw)

  def gridReferenceType      = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType"))
  def gridReference          = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference"))
  def gridReferencePrecision = parseGridRefPrecision(gridReferencePrecisionRaw)
  def gridReferencePrecisionRaw = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")
  def featureKey             = parseOptional(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey"))

  def parseOptional(value: String) = {
    // return Some(value) if the string is not null or empty, otherwise None
    Option(value).filter(_.trim.nonEmpty)
  }

  def parseGridRefPrecision(precision: String) = {
    if (precision == null || precision.isEmpty) {
      0
    }
    else {
      precision.toInt
    }
  }

  def parseSensitiveOccurrence(s: String) = {
    if (s == null) {
      false //default (missing column) means non sensitive
    }
    else {
      s.toBoolean
    }
  }

  def parseOccurrenceStatus(s: String) = {
    if (s == null) {
      false // default (missing column) means presence record
    }
    else {
      s.toLowerCase match {
        case "presence" => false
        case "absence" => true
        case _ => throw new ImportFailedException("Invalid occurrence status '%s'".format(s))
      }
    }
  }
}
