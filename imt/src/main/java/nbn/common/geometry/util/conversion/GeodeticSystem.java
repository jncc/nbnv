package nbn.common.geometry.util.conversion;

import nbn.common.pathfinding.DijkstraNode;
import java.util.ArrayList;
import java.util.Collection;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- The class which represents a GeodeticSystem
*/
public class GeodeticSystem implements DijkstraNode<GeodeticSystemConvertor>{
    private int id; //the id which allows this Geodetic System to be refered to
    private ArrayList<GeodeticSystemConvertor> conversionLinks;

    /**
    * The Constructor for a Geodetic System. Note that default access is used to deter from direct instantiation.
    * To obtain a GeodeticSystem object use the GeodeticSystemFactory
    * @param id The unique ID which represents this Geodetic system. This is required for use within a DijkstraMap
    * @see DijkstraMap
    * @see GeodeticSystemFactory
    */
    GeodeticSystem(int id) {
	this.id = id;
	conversionLinks = new ArrayList<GeodeticSystemConvertor>();
    }

    /**Gets the Unique ID for this Geodetic System
    * @return the Unique ID which represents this Geodetic System. This value is required by DijkstraMap
    * @see DijkstraMap
    */
    public int getID() { //each geodetic system needs a globally known id
	return id;
    }

    /**
    * Method to add a GeodeticSystemConvertor to this GeodeticSystem, to allow conversion from this system to another.
    *
    * It should be noted that it is not possible to add a GeodeticSystemConvertor which converts this GeodeticSystem to itself.
    * As it is assumed that a GeodeticSystem can convert to itself, with a 0 weight
    * @param convertor The Geodetic System Convertor to add to this Geodetic System
    * @throws IllegalArgumentException if the convertor attempting to be added does not convert from this geodetic system
    */
    public synchronized void addGeodeticSystemConvertor(GeodeticSystemConvertor convertor) {
	if(!equals(convertor.getStartingNode())) //if this is not equal to the convertors starting node, throw exception
	    throw new IllegalArgumentException("The convertor attempting to be added does not convert from this GeodeticSystem");
	conversionLinks.add(convertor); //add an element
    }

    /**
    * This method will return a collection of all known GeodeticSystemConvertors which this GeodeticSystem can convert to
    *
    * For thread safety reasons a clone of the Collection is returned,
    * this will ensure that any modification to this GeodeticSystem will not cause a ConcurrentModificationException
    * whilst this Collection is being iterated
    * @return the Collection of GeodeticSystemConvertors this Geodetic System can convert to
    */
    public synchronized Collection<GeodeticSystemConvertor> getConnectingLinks() { //returns as an clone for thread safety
	return new ArrayList<GeodeticSystemConvertor>(conversionLinks);
    }

    /** An equals method for this geodetic system,
    * Two geodetic systems are assumed to be equal if they have the same Unique id.
    * @param An object to compare this GeodeticSystem to
    * @return true if the input object is a GeodeticSystem with an id equal to this GeodeticSystem. Else, false.
    */
    public boolean equals(Object o) {
	if(o instanceof GeodeticSystem) {
	    GeodeticSystem toCompare = (GeodeticSystem)o;
	    return id == toCompare.id;
	}
	else
	    return false;
    }
}