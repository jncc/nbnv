package uk.org.nbn.nbnv

import java.util.Date

class Record(
  val recordKey: String,
  val surveyKey: String,
  val sampleKey: String,
  val startDate: Date,
  val endDate: Date,
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
