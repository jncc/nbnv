package uk.org.nbn.nbnv.importer.utility

object StringParsing {

  // unfortunately Scala doesn't define optionally failing conversions

  implicit def stringToMaybeParsableString(s: String) = new MaybeParsableString(s)

  class MaybeParsableString(s: String) {
    def maybeInt = try { Some(s.toInt) } catch { case ex: NumberFormatException => None }
    def maybeBoolean = try { Some(s.toBoolean) } catch { case ex: NumberFormatException => None }
  }
}
