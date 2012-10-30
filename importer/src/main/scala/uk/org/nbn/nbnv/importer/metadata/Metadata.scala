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
  val siteIsPublic: Boolean
  val recorderAndDeterminerArePublic: Boolean
  val publicResolution: Int

  override def toString = datasetKey
}
