package uk.org.nbn.nbnv.importer.records

import scala.collection.JavaConversions._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import java.text.SimpleDateFormat

/// Wraps a Darwin record in NBN clothing.
class NbnRecord(record: StarRecord) {

  // there should be exactly one extension record for a record (hence .head)
  private val extension = record.extension("http://uknbn.org/terms/NBNExchange").head

  // todo: we don't want to do parsing in this class
  // todo: .value (below) returns null if the column does not exist - should throw a better exception here

  private val format = new SimpleDateFormat("yyyy/MM/dd")
  
  def key =             record.core.value(DwcTerm.occurrenceID)
  def surveyKey =       record.core.value(DwcTerm.collectionCode)
  def sampleKey =       record.core.value(DwcTerm.eventID)
  def taxonVersionKey = record.core.value(DwcTerm.taxonID)
  def siteKey =         record.core.value(DwcTerm.locationID)
  def recorder =        record.core.value(DwcTerm.recordedBy)
  def determiner =      record.core.value(DwcTerm.identifiedBy)
  def attributes =      record.core.value(DwcTerm.dynamicProperties)

  def startDate              = format.parse(extension.value("http://uknbn.org/terms/startDate"))
  def endDate                = format.parse(extension.value("http://uknbn.org/terms/endDate"))
  def dateType               = extension.value("http://uknbn.org/terms/dateType")
  def sensitiveOccurrence    = extension.value("http://uknbn.org/terms/sensitiveOccurrence")
  def gridReferenceType      = extension.value("http://uknbn.org/terms/gridReferenceType")
  def gridReferencePrecision = extension.value("http://uknbn.org/terms/gridReferencePrecision")
}
