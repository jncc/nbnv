package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.records.{NbnRecord2, NbnRecord}
import util.parsing.json.JSON
import org.apache.log4j.Logger
import com.google.inject.Inject

class NbnRecordFactory @Inject()(log: Logger) {
  def makeRecord(rawData: List[String], metadata: ArchiveMetadata) : NbnRecord = {

    def getData(fieldIndex: Option[Int]) : Option[String] = {
      if (!fieldIndex.isDefined) { None }
      else {
        Option(rawData(fieldIndex.get)).filter(_.trim.nonEmpty)
      }
    }

    def parseOccurrenceStatus(s: Option[String]) = {
      s match {
        case None => false
        case Some(x) => x.toLowerCase match {
          case "presence" => false
          case "absence" => true
          case _ => {
            log.debug("Invalid Occurence status in record '%s'. Data should be rejected by validation".format(getData(metadata.key)))
            false
          }
        }
      }
    }

    def getAttributes(attributes: Option[String]) = {
      if (!attributes.isDefined) { Map.empty[String, String] }
      else {
        try {
          val json = JSON.parseFull(attributes.get)
          json.get.asInstanceOf[Map[String, String]]
        }
        catch {
          case e: Throwable => {
            log.debug("Improperly formed JSON attribute list in record '%s'. Data should be rejected by validation".format(getData(metadata.key)))
            Map.empty[String, String]
          }
        }
      }
    }

    new NbnRecord2 {
      val key = getData(metadata.key).get
      val absenceRaw = getData(metadata.absence)
      val absence = parseOccurrenceStatus(getData(metadata.absence))
      val surveyKey = getData(metadata.surveyKey)
      val sampleKey = getData(metadata.sampleKey)
      val taxonVersionKey = getData(metadata.taxonVersionKey)
      val siteKey = getData(metadata.siteKey)
      val siteName = getData(metadata.siteName)
      val recorder = getData(metadata.recorder)
      val determiner = getData(metadata.determiner)
      val eastRaw = getData(metadata.east)
      val east = getData(metadata.east) map { s => s.toDouble }
      val northRaw = getData(metadata.north)
      val north = getData(metadata.north) map { s => s.toDouble }
      val srs = getData(metadata.srs) map { s => s.toInt }
      val srsRaw = getData(metadata.srs)
      val attributesRaw = getData(metadata.attributes)
      val attributes = getAttributes(getData(metadata.attributes))
      val eventDateRaw = None
      val startDateRaw = None
      val startDate = None
      val endDateRaw = None
      val endDate = None
      val sensitiveOccurrenceRaw = None
      val sensitiveOccurrence = false
      val gridReferenceTypeRaw = None
      val gridReferenceRaw = None
      val gridReferencePrecision = None
      val gridReferencePrecisionRaw = None
      val featureKey = None
    }

    //todo: get rid of this temp return type.
    new NbnRecord(null)
  }


}
