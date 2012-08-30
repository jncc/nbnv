package nbn.common.geometry.util.conversion;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- A NullGeodeticSystemConvertor is capable of converting from itself. This is simple provided as a convenience.
*/
public class NullGeodeticSystemConvertor implements GeodeticSystemConvertor {
    private GeodeticSystem forSystem;

    /** The Constructor for the NullGeodeticSystem convertor. This will only take one GeodeticSystem, to which this convertor converts from and into
    * @param forSystem The GeodeticSystem this convertor converts from and to
    */
    public NullGeodeticSystemConvertor(GeodeticSystem forSystem) {
	this.forSystem = forSystem;
    }

    /**
    * Gets the GeodeticSystem which this Convertor converts from. Within a NullGeodeticSystemConvertor, this will be the same as the getEndingNode()
    * @return The Geodetic system this convertor converts from
    * @see #getEndingNode()
    */
    public final GeodeticSystem getStartingNode() {
	return forSystem;
    }

    /**
    * Gets the GeodeticSystem which this Convertor converts to. Within a NullGeodeticSystemConvertor, this will be the same as the getStartingNode()
    * @return The Geodetic system this convertor converts to
    * @see #getStartingNode()
    */
    public final GeodeticSystem getEndingNode() {
	return forSystem;
    }

    /**
    * Gets the linkweight of this NullGeodeticSystemConvertor. This will always return 0 since a perfect convertion is possible from the input to the output
    * As no processing is caried out
    * @see #convert(double[])
    * @return Always returns 0
    */
    public final int getLinkWeight() {
	return 0;
    }

    /** A NullGeodeticSystemConvertor always returns the array it is inputted with
    * @param input The x,y and z values for a convertion
    * @return the input to this convertion
    */
    public double[] convert(double[] input) {
	return input;
}
}