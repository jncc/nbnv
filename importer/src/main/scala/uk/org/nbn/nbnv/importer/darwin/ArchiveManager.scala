package uk.org.nbn.nbnv.importer.darwin

import org.gbif.dwc.text.{ArchiveFactory, Archive}
import java.io.File
import uk.org.nbn.nbnv.importer.Options
import com.google.inject.Inject

/// Manages Darwin archives via the GBIF Darwin Archive reader
class ArchiveManager @Inject()(options: Options) {

  def open(): Archive = {
    ArchiveFactory.openArchive(new File(options.archivePath), new File(options.tempDir))
  }
}


// here's how to read an NBN DwcA archive in scala

//  def read() {
//    try {
//      val archive = ArchiveFactory.openArchive(new File("c:\\work\\uk-dwca.zip"), new File("c:\\work\\deleteme"))
//
//      // list the terms in the archive
//      println("Terms in the archive: ")
//      for (term <- archive.getCore.getTerms) {
//        println("  " + term)
//      }
//      println("\n")
//
//      val metadataLocation = archive.getMetadataLocation
//      println(metadataLocation)
//
//      // read some values (but i wonder why they're using "iteratorRaw"??)
//      for (record <- archive.iteratorRaw) {
//        println("occurrenceID: " + record.core.value(DwcTerm.occurrenceID))
//        // in our case we know there should be exactly one extension record ("head" is first in a list)
//        val extensionRecord = record.extension("http://uknbn.org/terms/NBNExchange").head
//        for (extensionTerm <- extensionRecord.terms) {
//          println(extensionTerm.simpleName())
//        }
//        //        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        //        println("  sensitiveOccurrence: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        //        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//        //        println("  dateType: " + extensionRecord.value("http://uknbn.org/terms/dateType"))
//
//
//      }
//    }
//    catch {
//      case uae: UnsupportedArchiveException => uae.printStackTrace()
//      case e: Exception => e.printStackTrace()
//    }
//  }