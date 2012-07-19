package uk.org.nbn.nbnv

import org.gbif.dwc.text.{UnsupportedArchiveException, StarRecord, ArchiveFactory}
import java.io.File
import org.gbif.dwc.terms.{DwcTerm, ConceptTerm}
import scala.collection.JavaConversions._

/**
 * Reads Darwin Core Archive using the GBIF reader library.
 */

class Reader {
  def read() {
    try {
      val archive = ArchiveFactory.openArchive(new File("c:\\work\\uk-dwca.zip"), new File("c:\\work\\deleteme"))

      // list the terms in the archive
      println("Terms in the archive: ")
      for (term <- archive.getCore.getTerms) {
        println("  " + term)
      }
      println("\n")

      val metadataLocation = archive.getMetadataLocation
      println(metadataLocation)

      // read some values (but i wonder why they're using "iteratorRaw"??)
      for (record <- archive.iteratorRaw) {
        println("occurrenceID: " + record.core.value(DwcTerm.occurrenceID))
        // in our case we know there should be exactly one extension record ("head" is first in a list)
        val extensionRecord = record.extension("http://uknbn.org/terms/NBNExchange").head
        for (extensionTerm <- extensionRecord.terms) {
          println(extensionTerm.simpleName())
        }
//        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        println("  sensitiveOccurrence: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))


      }
    }
    catch {
      case uae: UnsupportedArchiveException => uae.printStackTrace()
      case e: Exception => e.printStackTrace()
    }
  }
}
