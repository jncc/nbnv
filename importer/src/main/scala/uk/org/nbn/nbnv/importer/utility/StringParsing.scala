package uk.org.nbn.nbnv.importer.utility

import java.text.{ParseException, SimpleDateFormat}

object StringParsing {

  // unfortunately Scala doesn't define optionally failing conversions

  implicit def stringToMaybeParsableString(s: String) = new MaybeParsableString(s)

  class MaybeParsableString(s: String) {
    def maybeInt = try { Some(s.toInt) } catch { case ex: NumberFormatException => None }
    def maybeBoolean = try { Some(s.toBoolean) } catch { case ex: NumberFormatException => None }
    def maybeDate(dateFormat: String) = {
      try {
        val sdf = new SimpleDateFormat(dateFormat)
        sdf.setLenient(false)
        Some(sdf.parse(s))
      } catch {
        case e: ParseException => None
      }
    }
    def isValidDate( dateFormatList: List[String]) = {

      var isValid = false

      dateFormatList.toStream
        .takeWhile(_ => isValid == false)
        .foreach(df => if( s.maybeDate(df) != None) isValid = true)

      isValid
    }
  }
}
