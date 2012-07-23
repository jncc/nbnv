/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv

import java.io.File
import javax.persistence.EntityManager
import scala.xml._

class MetadataImporter () {

  def Read(mdfile:File) {
    val data = XML.loadFile(mdfile)  
    /*<title>*</title> match {
     case <title>{ txt }</title> => println("title: " + txt)
     }*/
    /*val ns = data \ "title"
     println("title:" + ns.text)*/
    for (title <- data \\ "title"){
      println("title: " + title.text)
    }
    
    for (description <- data \\ "abstract" \ "para"){
      println("description: " + description.text)
    }
    
    for (rawConstraints <- data \\ "intellectualRights" \ "para"){
      val constraints = rawConstraints.text.replace("Access Constraints:", "").split("Use Constraints:")
      val accessConstraints = constraints(0).trim
      val useConstratints = constraints(1).trim
      println("accessConstraints: " + accessConstraints)
      println("useConstratints: " + useConstratints)
    }
    
    for (geographicCoverage <- data \\ "geographicCoverage" \ "geographicDescription"){
      println("geographicCoverage: " + geographicCoverage.text)
    }
    
    for (purpose <- data \\ "purpose" \ "para") {
      println("purpose: " + purpose.text)
    }
    
    for (method <- data \\ "methods" \ "methodStep" \ "description" \ "para") {
      println("method: " + method.text)
    }

    for (quality <- data \\ "methods" \ "qualityControl" \ "description" \ "para") {
      println("quality: " + quality.text)
    }    
  }
}
