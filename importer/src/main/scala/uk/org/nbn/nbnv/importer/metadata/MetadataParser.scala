package uk.org.nbn.nbnv.importer.metadata

import xml.Elem
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class MetadataParser {
  def parse(xml: Elem) : Metadata = {

    val dataset = (xml \ "dataset")

    val constraints = (dataset \ "intellectualRights" \ "para")
      .text.replace("accessConstraints:", "")
      .split("useConstraints:")

    // gets data from the additionalInfo elements using an identifier such as "Temporal Coverage:"
    def getAdditionalData(identifier: String) = {
      val paras = dataset \ "additionalInfo" \ "para"
      paras.find { _.text.startsWith(identifier) } match { // 'find' finds the first matching element
        case Some(para) => para.text.replace(identifier, "").trim
        case None => ""
      }
    }

    new Metadata {
      val datasetKey = (dataset \ "alternateIdentifier").text.trim
      val datasetTitle = (dataset \ "title").text.trim
      val datasetProviderName = (dataset \ "creator" \ "organizationName").text.trim
      val description = (dataset \ "abstract" \ "para").text.trim
      val accessConstraints = constraints(0).trim
      val useConstraints = constraints(1).trim
      val geographicCoverage = (dataset  \ "coverage" \ "geographicCoverage" \ "geographicDescription").text.trim
      val purpose = (dataset \ "purpose" \ "para").text.trim
      val dataCaptureMethod = (dataset \ "methods" \  "methodStep" \ "description" \ "para").text.trim
      val dataQuality = (dataset \ "methods" \ "qualityControl" \ "description" \ "para" ).text.trim
      val temporalCoverage = getAdditionalData("temporalCoverage:")
      val additionalInformation = getAdditionalData("additionalInformation:")
      val publicPrecision = getAdditionalData("publicPrecision:").maybeInt.getOrElse(0)
      val recorderAndDeterminerArePublic = getAdditionalData("recorderAndDeterminerNamesArePublic:").maybeBoolean.getOrElse(true)
      val attributesArePublic = getAdditionalData("recordAttributesArePublic:").maybeBoolean.getOrElse(true)
      val administratorForename = (dataset \ "metadataProvider" \ "individualName" \ "givenName").text.trim
      val administratorSurname = (dataset \ "metadataProvider" \ "individualName" \ "surName").text.trim
      val administratorEmail = (dataset \ "metadataProvider" \ "electronicMailAddress").text.trim
      val importType = getAdditionalData("importType:")
    }

  }
}
