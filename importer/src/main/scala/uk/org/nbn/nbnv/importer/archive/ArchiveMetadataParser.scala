package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.utility.FileSystem
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import xml.{Elem, Node}

class ArchiveMetadataParser  {
  def getMetadata(xml: Elem): ArchiveMetadata = {

    def getCoreField(term: String) = {
      getFieldIndex( "http://rs.tdwg.org/dwc/terms/", term)
    }

    def getExtensionField(term: String) = {
      getFieldIndex("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/", term)
    }

    def getFieldIndex(uriPrefix: String, term : String ): Option[Int] = {
      val uri = "%s%s".format(uriPrefix, term)
      val elem = ((xml \\ "field") filter {e => (e \ "@term").text == uri})
      if (elem.isEmpty) {
        None
      }
      else {
        Option((elem.head \ "@index").text.toInt)
      }
    }

    new ArchiveMetadata {
      val SkipHeaderLines: Option[Int] = (xml \ "core" \ "@ignoreHeaderLines").text.maybeInt
      val date: Option[Int] = getCoreField("eventDate")
      val startDate: Option[Int] = getExtensionField("eventDateStart")
      val gridReferenceType: Option[Int] = getExtensionField("gridReferenceType")
      val sensitiveOccurrence: Option[Int] = getExtensionField("sensitiveOccurrence")
      val siteName: Option[Int] = getCoreField("locality")
      val key: Option[Int] = getCoreField("occurrenceID")
      val gridReferencePrecision: Option[Int] = getExtensionField("gridReferencePrecision")
      val determiner: Option[Int] = getCoreField("identifiedBy")
      val recorder: Option[Int] = getCoreField("recordedBy")
      val siteKey: Option[Int] = getCoreField("locationID")
      val featureKey: Option[Int] = getExtensionField("siteFeatureKey")
      val attributes: Option[Int] = getCoreField("dynamicProperties")
      val srs: Option[Int] = getCoreField("verbatimSRS")
      val taxonVersionKey: Option[Int] = getCoreField("taxonID")
      val north: Option[Int] = getCoreField("verbatimLatitude")
      val dateType: Option[Int] = getExtensionField("eventDateTypeCode")
      val endDate: Option[Int] = getExtensionField("eventDateEnd")
      val gridReference: Option[Int] = getExtensionField("gridReference")
      val absence: Option[Int] = getCoreField("occurrenceStatus")
      val surveyKey: Option[Int] = getCoreField("collectionCode")
      val east: Option[Int] = getCoreField("verbatimLongitude")
      val sampleKey: Option[Int] = getCoreField("eventID")
    }
  }


}
