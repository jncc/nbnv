package uk.org.nbn.nbnv.metadata

import xml.Elem

class MetadataParser {
  def parse(xml: Elem) : Metadata = {

    val dataset = (xml \ "dataset")

    val constraints = (dataset \ "intellectualRights" \ "para")
      .text.replace("Access Constraints:", "")
      .split("Use Constraints:")

    // gets data from the additionalInfo elements. Specific data is identified in the text of the element
    // by an identifier such as Temporal Coverage:
    def getAdditionalData(identifier: String) = {
      val additionalInfoNodes = dataset \ "additionalInfo" \ "para"
      additionalInfoNodes.find { _.text.startsWith(identifier) } match {
        case Some(aiNode) => aiNode.text.replace(identifier, "").trim
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
      val temporalCoverage = getAdditionalData("Temporal Coverage:")
      val additionalInformation = getAdditionalData("Additional Information:")

      var prec = 10000
      try {
        prec = getAdditionalData("Public Access:").toInt
      } catch {
        case e: Exception => prec = 10000
      }
      val publicPrecision = prec

      var radap = false
      try {
        radap = getAdditionalData("Recorder Names:").toBoolean
      } catch {
        case e: Exception => radap = false
      }
      val recorderAndDeterminerArePublic = radap

      val siteIsPublic = true // todo


    }

  }
}
