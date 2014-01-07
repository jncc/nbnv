package uk.org.nbn.nbnv.importer.archive

abstract class ArchiveMetadata {
  val fields : Int
  val fieldSeparator : String
  val skipHeaderLines : Option[Int]
  val key: Option[Int]
  val absence: Option[Int]
  val surveyKey: Option[Int]
  val sampleKey: Option[Int]
  val taxonVersionKey: Option[Int]
  val siteKey: Option[Int]
  val siteName: Option[Int]
  val recorder: Option[Int]
  val determiner: Option[Int]
  val east: Option[Int]
  val north: Option[Int]
  val srs: Option[Int]
  val attributes: Option[Int]
  val date: Option[Int]
  val startDate: Option[Int]
  val endDate: Option[Int]
  val dateType: Option[Int]
  val sensitiveOccurrence: Option[Int]
  val gridReferenceType: Option[Int]
  val gridReference: Option[Int]
  val gridReferencePrecision: Option[Int]
  val featureKey: Option[Int]
}
