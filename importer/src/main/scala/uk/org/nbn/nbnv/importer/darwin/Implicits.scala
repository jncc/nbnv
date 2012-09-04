package uk.org.nbn.nbnv.importer.darwin

import org.gbif.dwc.text.StarRecord

object Implicits {
  implicit def StarRecordExtensions(starRecord : StarRecord) = new StarRecordExtensions(starRecord)
}
