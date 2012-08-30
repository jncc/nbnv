package nbn.common.geometry.util.conversion;

import nbn.common.pathfinding.*;
import java.util.Collection;
import nbn.common.util.logic.LogicTester;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- This class adapts the DijkstraMap to enable conversion from GeodeticSystem. 
* 
* The logic for this conversion is present within the GeodeticConvertor, which this class wraps
* @see GeodeticConvertor
* @see DijkstraMap
*/
public class GeodeticConverterHelper {
    private static DijkstraMap<GeodeticSystem,GeodeticSystemConvertor> geodeticConvertorMap;

    /**
    * A static method which gets the GeodeticSysetmConvertor for converting between the GeodeticSystems represented by startSystemID and endSystemID
    * @param startSystemID The Unique ID of the Geodetic start system
    * @param endSystemID The Unique ID of the Geodetic end system
    * @throws NoSuchConversionMethodException If there is no possible conversion between these two GeodeticSystems
    * @throws UnknownGeodeticSystemException If the the Map has not been correctly created or the input GeodeticSystems are not known
    */
    public static GeodeticSystemConvertor getGeodeticSystemConvertor(int startSystemID, int endSystemID) throws NoSuchConversionMethodException, UnknownGeodeticSystemException {
	return getGeodeticSystemConvertor(GeodeticSystemFactory.getGeodeticSystem(startSystemID), GeodeticSystemFactory.getGeodeticSystem(endSystemID));
    }

    /**
    * A static method which gets the GeodeticSysetmConvertor for converting between the input GeodeticSystems
    * @param startSystemID The Geodetic start system
    * @param endSystemID The Geodetic end system
    * @throws NoSuchConversionMethodException If there is no possible conversion between these two GeodeticSystems
    * @throws UnknownGeodeticSystemException If the the Map has not been correctly created or the input GeodeticSystems are not known
    */
    public static GeodeticSystemConvertor getGeodeticSystemConvertor(GeodeticSystem startSystem, GeodeticSystem endSystem) throws NoSuchConversionMethodException, UnknownGeodeticSystemException {
	initalise();
	try {
	    Collection<GeodeticSystemConvertor> conversionLinks = geodeticConvertorMap.getShortestPath(startSystem,endSystem);
	    if(conversionLinks.isEmpty())
		return new NullGeodeticSystemConvertor(startSystem); //allows conversion from this to this with no loss of accuracy
	    else
		return new CompositeGeodeticSystemConvertor(conversionLinks);
	}
	catch(NoPossiblePathException nppe) {
	    throw new NoSuchConversionMethodException("There is no possible conversion between " + startSystem + " and " + endSystem,nppe);
	}
	catch(UnknownDijkstraNodeException udne) {
	    throw new UnknownGeodeticSystemException("One of the input GeodeticSystems or a GeodeticSystem they can convert to is not known. Perhaps they have not been registered correctly?");
	}
    }
	
	
    private static synchronized void initalise() { //here I wil create the map which can be navigated using dijkstras algorithm
	if(geodeticConvertorMap == null) { //if not already initalised, initalise and create all known convertors
	    geodeticConvertorMap = new DijkstraMap<GeodeticSystem,GeodeticSystemConvertor>();//build up map
	    try {
		for(GeodeticSystem currGeoSystem : GeodeticSystemFactory.getGeodeticSystems())//add systems to map
		    geodeticConvertorMap.addDijkstraNode(currGeoSystem);
	    }
	    catch(DijkstraNodeAlreadyKnownException gsake) {
		LogicTester.performLogicTest(false); //logic dictates this should not occur
	    }
	}
    }
}