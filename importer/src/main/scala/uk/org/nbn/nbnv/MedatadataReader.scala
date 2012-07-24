/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv

import java.io.File
import scala.xml._

class MetadataReader {
  def GetMetaData(metadataFile:File) : Metadata = {
    val data = XML.loadFile(metadataFile)
    
    val dataset = (data \ "dataset")
    
    val constraints = (dataset \ "intellectualRights" \ "para")
                       .text.replace("Access Constraints:", "")
                       .split("Use Constraints:");
                       
    new Metadata {
      val datasetKey = (dataset \ "alternateIdentifier").text.trim
      val datsetTitle = (dataset \ "title").text.trim
      val description = (dataset \ "abstract" \ "para").text.trim
      val accessConstraints = constraints(0).trim
      val useConstraints = constraints(1).trim
      val geographicCoverage = (dataset  \ "coverage" \ "geographicCoverage" \ "geographicDescription").text.trim
      val purpose = (dataset \ "purpose" \ "para").text.trim
      val method = (dataset \ "methods" \  "methodStep" \ "description" \ "para").text.trim
      val quality = (dataset \ "methods" \ "qualityControl" \ "description" \ "para" ).text.trim
    }
  }
}
