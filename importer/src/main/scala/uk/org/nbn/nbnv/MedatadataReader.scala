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
    
    val datasetKey = (data \\ "alternateIdentifier").text
    val datsetTitle = (data \\ "title").text
    val description = (data \\ "abstract" \ "para").text
    
    val constraints = (data \\ "intellectualRights" \ "para")
                       .text.replace("Access Constraints:", "")
                       .split("Use Constraints:");
                       
    val accessConstraints = constraints(0).trim
    val useConstratints = constraints(1).trim
    
    val geographicCoverage = (data \\ "geographicCoverage" \ "geographicDescription").text
    
    val purpose = (data \\ "purpose" \ "para").text
    
    val method = (data \\ "methods" \ "methodStep" \ "description" \ "para").text
    
    val quality = (data \\ "methods" \ "qualityControl" \ "description" \ "para").text
    
    new Metadata(
      datasetKey,
      datsetTitle,
      description,
      accessConstraints,
      useConstratints,
      geographicCoverage,
      purpose,
      method,
      quality
    )
  }
}
