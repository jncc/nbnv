package uk.org.nbn.nbnv.importer.metadata

/// The "mode" of import.
object Mode extends Enumeration {
  val upsert = Value("upsert")
  val append = Value("append")

  def stringToMaybeValue(value: String) = {
    try {
      Some(Mode.withName(value.toLowerCase))
    } catch {
      case ex: NoSuchElementException => None
    }
  }
}