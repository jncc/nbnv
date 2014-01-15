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

  test("should parse a record") {
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
      "FeatureKeyTest",
      "absence"
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
      val siteKey: Option[Int] = Some(8)
      val fields: Int = 24
      val attributes: Option[Int] = Some(18)
      val srs: Option[Int] = Some(19)
      val fieldSeparator: String = "\\t"
      val taxonVersionKey: Option[Int] = Some(6)
      val north: Option[Int] = Some(21)
      val dateType: Option[Int] = Some(5)
      val endDate: Option[Int] = Some(4)
      val gridReference: Option[Int] = Some(11)
      val absence: Option[Int] = Some(24)
      val surveyKey: Option[Int] = Some(1)
      val east: Option[Int] = Some(20)
      val sampleKey: Option[Int] = Some(2)
    }

    var record = factory.makeRecord(rawRecord, metadata)

    record.eventDateRaw should be (Some("13/07/2001"))
    record.startDateRaw should be (Some("19/07/2001"))
    record.endDateRaw should be (Some("21/07/2001"))
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
    record.siteKey should be (Some("CI0000030000034X"))
    record.attributesRaw should be (Some("{\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}"))
    record.attributes.size should be (3)
    record.attributes("SampleMethod") should be ("Field Observation")
    record.srsRaw should be (Some("4326"))
    record.srs should be (Some(4326))
    record.taxonVersionKey should be (Some("NBNSYS0000006640"))
    record.northRaw should be (Some("52.585667"))
    record.north should be (Some(52.585667))
    record.eastRaw should be (Some("-0.22174100"))
    record.east should be (Some(-0.22174100))
    record.dateType should be (Some("D"))
    record.gridReferenceRaw should be (None)
    record.absenceRaw should be (Some("absence"))
    record.absence should be (true)
    record.surveyKey should be (Some("CI0000030000000A"))
    record.sampleKey should be (Some("CI000003000003T3"))
  }

  test("record factory should return None for unparsable data")
  {
    val rawRecord = List(
      "CI00000300000TNR",
      "CI0000030000000A",
      "CI000003000003T3",
      "19/07/2001",
      "21/07/2001",
      "D",
      "NBNSYS0000006640",
      "badOccurrence",
      "CI0000030000034X",
      "Sherwood Forest Holiday Village: lake 4",
      "",
      "",
      "badPrecision",
      "Robert Merritt",
      "Robert Merritt dt",
      "Field Observation",
      "",
      "",
      "{\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}",
      "badSRS",
      "badEast",
      "badNorth",
      "13/07/2001",
      "FeatureKeyTest",
      "absence"
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
      val siteKey: Option[Int] = Some(8)
      val fields: Int = 24
      val attributes: Option[Int] = Some(18)
      val srs: Option[Int] = Some(19)
      val fieldSeparator: String = "\\t"
      val taxonVersionKey: Option[Int] = Some(6)
      val north: Option[Int] = Some(21)
      val dateType: Option[Int] = Some(5)
      val endDate: Option[Int] = Some(4)
      val gridReference: Option[Int] = Some(11)
      val absence: Option[Int] = Some(24)
      val surveyKey: Option[Int] = Some(1)
      val east: Option[Int] = Some(20)
      val sampleKey: Option[Int] = Some(2)
    }

    var record = factory.makeRecord(rawRecord, metadata)

    record.east should be (None)
    record.eastRaw should be (Some("badEast"))

    record.north should be (None)
    record.northRaw should be (Some("badNorth"))

    record.srs should be (None)
    record.srsRaw should be (Some("badSRS"))

    record.sensitiveOccurrence should be (false)
    record.sensitiveOccurrenceRaw should be (Some("badOccurrence"))

    record.gridReferencePrecision should be (None)
    record.gridReferencePrecisionRaw should be (Some("badPrecision"))
  }


}
