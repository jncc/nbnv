package uk.org.nbn.nbnv.metadata

import xml.Elem

class MetadataParser {
  def parse(xml: Elem) : Metadata = {

    val dataset = (xml \ "dataset")

    val constraints = (dataset \ "intellectualRights" \ "para")
      .text.replace("Access Constraints:", "")
      .split("Use Constraints:");

    new Metadata {
      val datasetKey = (dataset \ "alternateIdentifier").text.trim
      val datasetTitle = (dataset \ "title").text.trim
      val description = (dataset \ "abstract" \ "para").text.trim
      val accessConstraints = constraints(0).trim
      val useConstraints = constraints(1).trim
      val geographicCoverage = (dataset  \ "coverage" \ "geographicCoverage" \ "geographicDescription").text.trim
      val purpose = (dataset \ "purpose" \ "para").text.trim
      val dataCaptureMethod = (dataset \ "methods" \  "methodStep" \ "description" \ "para").text.trim
      val dataQuality = (dataset \ "methods" \ "qualityControl" \ "description" \ "para" ).text.trim
    }
  }
}
