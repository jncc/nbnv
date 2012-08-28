package uk.org.nbn.nbnv.importer.records

import scala.collection.JavaConversions._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.ImportFailedException

/// Wraps a Darwin record in NBN clothing.
class NbnRecord(record: StarRecord) {

  // there should be exactly one extension record for a record (hence .head)
  private val extension = record.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence").head

  // todo: we don't want to do parsing in this class
  // todo: .value (below) returns null if the column does not exist - should throw a better exception here

  private val format = new SimpleDateFormat("yyyy/MM/dd")
  
  def key =             record.core.value(DwcTerm.occurrenceID)
  def absence =         parseOccurrenceStatus(record.core.value(DwcTerm.occurrenceStatus))
  def surveyKey =       record.core.value(DwcTerm.collectionCode)
  def sampleKey =       record.core.value(DwcTerm.eventID)
  def taxonVersionKey = record.core.value(DwcTerm.taxonID)
  def siteKey =         record.core.value(DwcTerm.locationID)
  def siteName =        record.core.value(DwcTerm.locality)
  def recorder =        record.core.value(DwcTerm.recordedBy)
  def determiner =      record.core.value(DwcTerm.identifiedBy)
  def attributes =      record.core.value(DwcTerm.dynamicProperties)

  def startDate              = format.parse(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart"))
  def endDate                = format.parse(extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd"))
  def dateType               = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")
  def sensitiveOccurrence    = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence").toBoolean
  def gridReferenceType      = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")
  def gridReference          = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")
  def gridReferencePrecision = extension.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecision")

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
