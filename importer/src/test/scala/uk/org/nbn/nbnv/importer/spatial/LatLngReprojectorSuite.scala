package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class LatLngReprojectorSuite extends BaseFunSuite {
  test("should give correct easting & northing for Lat 60.157057 lng -1.1515654 in the OSGB projection") {
    val rpj = new LatLngReprojector
    val (easting, northing) = rpj.reproject(60.157057, -1.1515654, "27700")

    easting should be (447200 plusOrMinus 50)
    northing should be (1141700 plusOrMinus 50)
  }

  test("should give correct easting & northing for Lat 53.351706 lng -6.2509854 in the OSNI projection") {
    val rpj = new LatLngReprojector
    val (easting, northing) = rpj.reproject(53.351706, -6.2509854, "29903")

    println("Easting: " + easting.toString)
    println("Northing: " + northing.toString)
    easting should be (316500 plusOrMinus 50)
    northing should be (234900 plusOrMinus 50)
  }

  //todo add channel islands test

}
