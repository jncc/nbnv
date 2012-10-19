package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.spatial.{ChannelIslandGridSquareInfo, IrishGridSquareInfo, IrishGrid, BritishGridSquareInfo}
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv87Validator {
  def validate(record: NbnRecord) = {
    val easting = record.east.get.toInt
    val northing =  record.north.get.toInt

    val (result, error) =
      record.srs.get match {
        case 27700 => BritishGridSquareInfo.testEastingNorthing(easting, northing)
        case 29903 => IrishGridSquareInfo.testEastingNorthing(easting, northing)
        case 23030 => ChannelIslandGridSquareInfo.testEastingNorthing(easting, northing)
      }

    if (result == true) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "Validated: The easting and northing are valid for the specified SRS"
      }
    }
    else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = error.getOrElse("The easting and northing are invalid")
      }
    }
  }
}
