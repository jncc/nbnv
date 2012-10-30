package uk.org.nbn.nbnv.importer

/// Represents an import failure due to bad source data.
case class ImportFailedException(message: String) extends Exception(message)
