package uk.org.nbn.nbnv

class RecordParser {
  def parse(line: String):Record = {
    val splat = line.split("\t", -1).map(s => s.trim()) // -1 to retain empty strings!
    val format = new java.text.SimpleDateFormat("dd/MM/yyyy")

    new Record(
      recordKey = splat(0),
      surveyKey = splat(1),
      sampleKey	 = splat(2),
      startDate = format.parse(splat(3)),
      endDate = format.parse(splat(4)),
      dateType = splat(5),
      taxonVersionKey = "",
      sensitive = false,
      siteKey = "",
      siteName = "",
      projection = "",
      gridReference = "",
      precision = "",
      recorder = "",
      determiner = "",
      sampleMethod = "",
      comment = "",
      abundance = "")
  }
}
