package uk.org.nbn.nbnv.importer.darwin

import uk.org.nbn.nbnv.importer.darwin.NbnFields._
import org.gbif.dwc.text.StarRecord

import scala.collection.JavaConversions._


class StarRecordExtensions(starRecord: StarRecord) {
  //Maps NBN Field ot DWC Term

  def getCoreField(fieldName: NbnFields) = {


    FieldMaps.coreFieldMap.get(fieldName) match {
      case Some(term) => starRecord.core.value(term)
      case None => throw new Exception("Unmapped core term")
    }
  }

  def getExtensionField(fieldName: NbnFields) = {

    FieldMaps.extensionFieldMap.get(fieldName) match {
      case Some(term) => {
        val extension = starRecord.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence").head
        extension.value(FieldMaps.extensionTermUri + term)
      }
      case None => throw new Exception("Unmapped extension term")
    }
  }
}
