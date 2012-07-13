package uk.org.nbn.nbnv

class Record(
  val recordKey: String,
  val surveyKey: String,
  val sampleKey	: String,
  val startDate: String,
  val endDate: String,
  val dateType: String,
  val taxonVersionKey: String,
  val sensitive: Boolean,
  val siteKey: String,
  val siteName: String,
  val projection: String,
  val gridReference: String,
  val precision: String,
  val recorder: String,
  val determiner: String,
  val sampleMethod: String,
  val comment: String,
  val abundance: String) {
}
