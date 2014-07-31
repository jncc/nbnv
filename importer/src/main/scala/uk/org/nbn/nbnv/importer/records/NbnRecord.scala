package uk.org.nbn.nbnv.importer.records

import java.util.Date

import uk.org.nbn.nbnv.importer.BadDataException

/// Wraps a Darwin record in NBN clothing.

abstract class NbnRecord() {
  val key : String
  val absenceRaw : Option[String]
  val absence : Boolean
  val surveyKey : Option[String]
  val sampleKey : Option[String]
  val taxonVersionKey : Option[String]
  val siteKey : Option[String]
  val siteName : Option[String]
  val recorder : Option[String]
  val determiner : Option[String]
  val eastRaw : Option[String]
  val east : Option[Double]
  val northRaw : Option[String]
  val north : Option[Double]
  val srs : Option[Int]
  val srsRaw : Option[String]
  val attributesRaw : Option[String]
  val attributes : Map[String, String]

  val eventDateRaw : Option[String]
  val startDateRaw : Option[String]
  val startDate : Option[Date]
  val endDateRaw : Option[String]
  val endDate : Option[Date]

  val sensitiveOccurrenceRaw : Option[String]
  val sensitiveOccurrence : Boolean

  val gridReferenceTypeRaw : Option[String]
  val gridReferenceRaw : Option[String]
  val gridReferencePrecision : Option[Int]
  val gridReferencePrecisionRaw : Option[String]
  val featureKey : Option[String]
  val dateType : Option[String]
  val dateTypeRaw : Option[String]

  def feature = {
    if (gridReferenceRaw.isDefined)
      GridRefDef(gridReferenceRaw.get, parseSpatialSystem, gridReferencePrecision)
    else if (featureKey.isDefined)
      BoundaryDef(featureKey.get)
    else if (east.isDefined && north.isDefined && (srs.isDefined || gridReferenceTypeRaw.isDefined))
      PointDef(east.get, north.get, parseSpatialSystem.get, gridReferencePrecision)
    else
      throw new BadDataException("Couldn't parse feature.")
  }

  private def parseSpatialSystem = {
    if (gridReferenceTypeRaw.isDefined)
      Some(GridTypeDef(gridReferenceTypeRaw.get))
    else if (srs.isDefined)
      Some(SrsDef(srs.get))
    else
      None
  }
}

