package uk.org.nbn.nbnv

import java.text.ParseException

class RecordParser {
  val format = new java.text.SimpleDateFormat("dd/MM/yyyy")
  def parse(line: String) = {
    val splat = line
      .split("\t", -1) // -1 to retain empty strings!
      .map(_.trim)
    new Record(
      recordKey = splat(0),
      surveyKey = splat(1),
      sampleKey	 = splat(2),
      startDate = format.parse(splat(3)),
      endDate = format.parse(splat(4)),
      dateType = splat(5),
      taxonVersionKey = splat(6),
      sensitive = parseBool(splat(7)),
      siteKey = splat(8),
      siteName = splat(9),
      projection = splat(10),
      gridReference = splat(11),
      precision = splat(12),
      recorder = splat(13),
      determiner = splat(14),
      sampleMethod = splat(15),
      comment = splat(16),
      abundance = splat(17))
  }

  def parseBool(s: String) = s match {
    case "T" => true
    case "F" => false
    case _  => throw new ParseException("Bad", 0)
  }
}
