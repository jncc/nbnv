package uk.org.nbn.nbnv.importer.darwin

object NbnFields extends Enumeration {
  type NbnFields = Value
  val key
  , absence
  , surveyKey
  , sampleKey
  , taxonVersionKey
  , siteKey
  , siteName
  , recorder
  , determiner
  , attributeJSON
  , startDate
  , endDate
  , dateType
  , sensitiveOccurrence
  , gridReferenceType
  , gridReference
  , gridReferencePrecision
  = Value
}
