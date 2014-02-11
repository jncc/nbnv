package uk.org.nbn.nbnv.importer.archive

import java.io._

import scala.io.Source

class CsvReader(file:File) extends Iterable[List[String]]{

  def iterator: Iterator[List[String]] = {
    Source.fromFile(file).getLines().map(l => l.split("\t").toList)
  }
}

//
//import org.supercsv.io._
//import org.supercsv.prefs._
//import scala.collection.JavaConversions._
//class CsvReader(file:File) extends Iterable[List[String]]{
//
//  def iterator = new Iterator[List[String]]{
//    private lazy val prefs = new CsvPreference.Builder('"','\t', "\r\n").build()
//    private lazy val reader = new CsvListReader(new FileReader(file),prefs)
//
//    private var current:List[String] = null
//
//    def next():List[String] = current
//    def hasNext():Boolean = {
//      val line = reader.read()
//      if(line == null){
//        reader.close
//        false
//      }
//      else{
//        current = line.toList
//        true
//      }
//    }
//  }
//}
