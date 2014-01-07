package uk.org.nbn.nbnv.importer.archive

import java.io._
import org.supercsv.io._
import org.supercsv.prefs._
import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import uk.org.nbn.nbnv.importer.Options

class CSVReader(file:File, options: Options) extends Iterable[List[String]]{

  def iterator = new Iterator[List[String]]{
    private lazy val reader = new CsvListReader(new FileReader(file),CsvPreference.EXCEL_PREFERENCE)
//    private lazy val reader = new CsvMapReader(new FileReader(file),CsvPreference.EXCEL_PREFERENCE)
    reader.getHeader(true)

    private var current:List[String] = null

    def next():List[String] = current
    def hasNext():Boolean = {
      val line = reader.read().toList
      if(line == null){
        reader.close
        false
      }
      else{
        current = line
        true
      }
    }
  }
}
