package uk.org.nbn.nbnv.importer.spatial

import uk.me.jstott.jcoord._
import datum.Datum
import ellipsoid.{InternationalEllipsoid, Hayford1910Ellipsoid}

//ERP50-W Datum
//seven parameter transform parameters to WGS84
//sourced from here: http://fmepedia.safe.com/articles/FAQ/ED50toWGS84

//getDs()
//
//double	getDx()
//
//double	getDy()
//
//double	getDz()
//
//java.lang.String	getName()
//Get the name of this Datum.
//Ellipsoid	getReferenceEllipsoid()
//Get the reference ellipsoid associated with this Datum.
//double	getRx()
//
//double	getRy()
//
//double	getRz()
//
//java.lang.String	toString()


class ED50Datum extends Datum{
  override def getName() = "European Datum 1950 (ERP50-W, ED50)"
  override def getReferenceEllipsoid() = InternationalEllipsoid.getInstance()
  override def getDs() = 9.39
  override def getDx() = -131.0
  override def getDy() = -100.3
  override def getDz() = -163.4
  override def getRx() = 1.244
  override def getRy() = 0.02
  override def getRz() = 1.144
  override def toString() = "%s [semi-major axis = %s, semi-minor axis = %s] dx=%s dy=%s dz=%s ds=%s rx=%s ry=%s rz=%s"
    .format(getName
    ,getReferenceEllipsoid.getSemiMajorAxis
    ,getReferenceEllipsoid.getSemiMinorAxis
    ,getDx,getDy,getDz,getDs
    ,getRx,getRy,getRz)
}
