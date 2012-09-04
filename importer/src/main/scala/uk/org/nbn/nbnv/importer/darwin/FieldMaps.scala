package uk.org.nbn.nbnv.importer.darwin

import uk.org.nbn.nbnv.importer.darwin.NbnFields._
import org.gbif.dwc.terms.DwcTerm._

object FieldMaps {
  val coreFieldMap = Map(
    key -> occurrenceID
    ,absence -> occurrenceStatus
    ,surveyKey -> collectionCode
    ,sampleKey -> eventID
    ,taxonVersionKey -> taxonID
    ,siteKey -> locationID
    ,siteName -> locality
    ,recorder -> recordedBy
    ,determiner -> identifiedBy
    ,attributeJSON -> dynamicProperties
  )

  val extensionTermUri = "http://rs.nbn.org.uk/dwc/nxf/0.1/terms/"

  val extensionFieldMap = Map(
    startDate -> "eventDateStart"
    ,endDate -> "eventDateEnd"
    ,dateType -> "eventDateTypeCode"
    ,sensitiveOccurrence -> "sensitiveOccurrence"
    ,gridReferenceType -> "gridReferenceType"
    ,gridReference -> "gridReference"
    ,gridReferencePrecision -> "gridReferencePrecision"
  )
}
