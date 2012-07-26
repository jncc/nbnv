package uk.org.nbn.nbnv.metadata

abstract class Metadata {
  val datasetKey: String
  val datasetTitle: String
  val description: String
  val accessConstraints: String
  val useConstraints: String
  val geographicCoverage: String
  val purpose: String
  val method: String
  val quality: String

  override def toString = datasetKey
}
