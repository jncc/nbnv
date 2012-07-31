package uk.org.nbn.nbnv.importer.records

import scala.collection.JavaConversions._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord

/// Wraps a Darwin record in NBN clothing.
class NbnRecord(record: StarRecord) {

  // there should be exactly one extension record for a record (hence .head)
  val extension = record.extension("http://uknbn.org/terms/NBNExchange").head

  def key =             record.core.value(DwcTerm.occurrenceID)
  def surveyKey =       record.core.value(DwcTerm.collectionCode)
  def sampleKey =       record.core.value(DwcTerm.eventID)
  def startDate =       record.core.value(DwcTerm.eventDate)
  def taxonVersionKey = record.core.value(DwcTerm.taxonID)
  def siteKey =         record.core.value(DwcTerm.locationID)
  def recorder =        record.core.value(DwcTerm.recordedBy)
  def determiner =      record.core.value(DwcTerm.identifiedBy)

  def dateType               = extension.value("http://uknbn.org/terms/dateType")
  def sensitiveOccurrence    = extension.value("http://uknbn.org/terms/sensitiveOccurrence")
  def gridReferenceType      = extension.value("http://uknbn.org/terms/gridReferenceType")
  def gridReferencePrecision = extension.value("http://uknbn.org/terms/gridReferencePrecision")
}
