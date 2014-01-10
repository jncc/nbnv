package uk.org.nbn.nbnv.importer.archive

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.apache.log4j.Logger

class NbnRecordFactorySuite extends BaseFunSuite with BeforeAndAfter {
  var factory : NbnRecordFactory = _

  before {
    val log = mock[Logger]

    factory = new NbnRecordFactory(log)
  }

  test("should parse a valid record") {
    val rawRecord = List(
      "CI00000300000TNR",
      "CI0000030000000A",
      "CI000003000003T3",
      "19/07/2001",
      "21/07/2001",
      "D",
      "NBNSYS0000006640",
      "false",
      "CI0000030000034X",
      "Sherwood Forest Holiday Village: lake 4",
      "",
      "",
      "100",
      "Robert Merritt",
      "Robert Merritt dt",
      "Field Observation",
      "",
      "",
      "{\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}",
      "4326",
      "-0.22174100",
      "52.585667",
      "13/07/2001",
      "FeatureKeyTest"
    )

    val metadata = new ArchiveMetadata {
      val date: Option[Int] = Some(22)
      val startDate: Option[Int] = Some(3)
      val gridReferenceType: Option[Int] = Some(10)
      val sensitiveOccurrence: Option[Int] = Some(7)
      val siteName: Option[Int] = Some(9)
      val key: Option[Int] = Some(0)
      val gridReferencePrecision: Option[Int] = Some(12)
      val skipHeaderLines: Option[Int] = None
      val determiner: Option[Int] = Some(14)
      val recorder: Option[Int] = Some(13)
      val featureKey: Option[Int] = Some(23)
      val siteKey: Option[Int] = None
      val fields: Int = 24
      val attributes: Option[Int] = None
      val srs: Option[Int] = None
      val fieldSeparator: String = ""
      val taxonVersionKey: Option[Int] = None
      val north: Option[Int] = None
      val dateType: Option[Int] = None
      val endDate: Option[Int] = None
      val gridReference: Option[Int] = None
      val absence: Option[Int] = None
      val surveyKey: Option[Int] = None
      val east: Option[Int] = None
      val sampleKey: Option[Int] = None
    }

    var record = factory.makeRecord(rawRecord, metadata)

    record.eventDateRaw should be (Some("13/07/2001"))
    record.startDateRaw should be (Some("19/07/2001"))
    record.gridReferenceTypeRaw should be (None)
    record.sensitiveOccurrenceRaw should be (Some("false"))
    record.sensitiveOccurrence should be (false)
    record.siteName should be (Some("Sherwood Forest Holiday Village: lake 4"))
    record.key should be ("CI00000300000TNR")
    record.gridReferencePrecisionRaw should be (Some("100"))
    record.gridReferencePrecision should be (Some(100))
    record.determiner should be (Some("Robert Merritt dt"))
    record.recorder should be (Some("Robert Merritt"))
    record.featureKey should be (Some("FeatureKeyTest"))
  }

}
