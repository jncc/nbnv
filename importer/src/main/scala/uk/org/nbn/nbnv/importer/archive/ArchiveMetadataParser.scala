package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.utility.FileSystem
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import xml.{Elem, Node}

class ArchiveMetadataParser  {
  def getMetadata(xml: Elem): ArchiveMetadata = {

    def getFieldIndex(isCore: Boolean, term : String ): Option[Int] = {
      val uri = "%s%s".format((if (isCore)
        "http://rs.tdwg.org/dwc/terms/" else "http://rs.nbn.org.uk/dwc/nxf/0.1/terms/")
        , term)
      val elem = ((xml \\ "field") filter {e => (e \ "@term").text == uri})
      if (elem.isEmpty) {
        None
      }
      else {
        (elem.head \ "@index").text.maybeInt
      }
    }

    new ArchiveMetadata {
      val SkipHeaderLines: Option[Int] = (xml \ "core" \ "@ignoreHeaderLines").text.maybeInt
      val date: Option[Int] = getFieldIndex(true, "eventDate")
      val startDate: Option[Int] = getFieldIndex(true, "wibble")
      val gridReferenceType: Option[Int] = None
      val sensitiveOccurrence: Option[Int] = None
      val siteName: Option[Int] = None
      val key: Option[Int] = None
      val gridReferencePrecision: Option[Int] = None
      val determiner: Option[Int] = None
      val recorder: Option[Int] = None
      val siteKey: Option[Int] = None
      val featureKey: Option[Int] = None
      val attributes: Option[Int] = None
      val srs: Option[Int] = None
      val taxonVersionKey: Option[Int] = None
      val north: Option[Int] = None
      val dateType: Option[Int] = None
      val endDate: Option[Int] = None
      val gridReference: Option[Int] = None
      val absence: Option[Int] = None
      val surveyKey: Option[Int] = None
      val east: Option[Int] = None
      val sampleKey: Option[Int] = None
    }
  }


}
