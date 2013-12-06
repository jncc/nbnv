package uk.org.nbn.nbnv.importer.metadata

abstract class Metadata {

  val datasetKey: String
  val datasetTitle: String
  val datasetProviderName: String
  val description: String
  val accessConstraints: String
  val useConstraints: String
  val geographicCoverage: String
  val purpose: String
  val dataCaptureMethod: String
  val dataQuality: String
  val temporalCoverage: String
  val additionalInformation: String
  val recorderAndDeterminerArePublic: Boolean
  val attributesArePublic: Boolean
  val publicPrecision: Int
  val administratorForename: String
  val administratorSurname: String
  val administratorEmail: String
  val importType: String

  override def toString = datasetKey
}
