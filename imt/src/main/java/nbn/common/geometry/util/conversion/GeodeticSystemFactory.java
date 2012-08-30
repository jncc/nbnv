package nbn.common.geometry.util.conversion;

import java.util.TreeMap;
import java.util.Collection;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- Area to witch the Geodetic Systems are defined and linked together
*/
public class GeodeticSystemFactory {
    /** The unique ID for the WGS84 GeodeticSystem*/
    public static final int WGS84 = 0;
    /** The unique ID for the OSI GeodeticSystem*/
    public static final int OSI = 1;
    /** The unique ID for the OSGB GeodeticSystem*/
    public static final int OSGB = 2;
    /** The unique ID for the IRELAND65 GeodeticSystem*/
    public static final int IRELAND65 = 3;
    /** The unique ID for the OSGB36 GeodeticSystem*/
    public static final int OSGB36 = 4;

    private static TreeMap<Integer,GeodeticSystem> geoSystems;

    /**
     * This method will return the required Geodetic System given a particular Geodetic System ID.
     * @param id Of the Geodetic System required.
     * @return The GeodeticSystem which represents the system of the given id.
     * @throws UnknownGeodeticSystemException If no GeodeticSystem has been defined with that id.
     */
    public static GeodeticSystem getGeodeticSystem(int id) throws UnknownGeodeticSystemException{
	initalise();
	GeodeticSystem toReturn = geoSystems.get(id);
	if(toReturn == null)
	    throw new UnknownGeodeticSystemException("The requested geodetic system is unknown");
	else
	    return toReturn;
    }

    /**
     * Returns the collection of all the Geodetic Systems this factory is capable of creating
     * @return The collection of GeodeticSystems which this Factory can create
     */
    public static Collection<GeodeticSystem> getGeodeticSystems() {
	initalise();
	return geoSystems.values();
    }

    private synchronized static void initalise() {
	if(geoSystems == null) {
	    geoSystems = new TreeMap<Integer,GeodeticSystem>();
	    //define systems
	    GeodeticSystem wgs84 = new GeodeticSystem(WGS84);
	    GeodeticSystem osi = new GeodeticSystem(OSI);
	    GeodeticSystem osgb = new GeodeticSystem(OSGB);
	    GeodeticSystem ireland65 = new GeodeticSystem(IRELAND65);
	    GeodeticSystem osgb36 = new GeodeticSystem(OSGB36);

	    //define convertors for wgs84
	    wgs84.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(wgs84,osgb) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.WGS84toOSGBGrid(input);
		}
	    });
	    wgs84.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(wgs84,osi) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.WGS84toOSIGrid(input);
		}
	    });

	    //define convertors for osi
	    osi.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osi,wgs84) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSItoWGS84(input);
		}
	    });
	    osi.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osi,osgb) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSItoOSGB(input);
		}
	    });
	    osi.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osi,ireland65) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSIGridToLongLatH(input);
		}
	    });

	    //define convertors for osgb
	    osgb.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osgb,wgs84) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSGBtoWGS84(input);
		}
	    });
	    osgb.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osgb,osi) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSGBtoOSI(input);
		}
	    });
	    osgb.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osgb,osgb36) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.OSGBGridToLongLatH(input);
		}
	    });

	    //define convertors for ireland65
	    ireland65.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(ireland65,osi) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.projectToOSI(input);
		}
	    });

	    //define convertors for osgb36
	    osgb36.addGeodeticSystemConvertor(new SkeletonGeodeticSystemConvertor(osgb36,osgb) {
		public double[] convert(double[] input) {
		    return GeodeticConverter.projectToOSGB(input);
		}
	    });

	    geoSystems.put(WGS84,wgs84);
	    geoSystems.put(OSI,osi);
	    geoSystems.put(OSGB,osgb);
	    geoSystems.put(IRELAND65,ireland65);
	    geoSystems.put(OSGB36,osgb36);
	}
    }
}
