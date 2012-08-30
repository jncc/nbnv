package nbn.common.geometry.util.conversion;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :- This defines the standard skeleton which a GeodeticSystemConvertor could take, this is mearly a conveniance for annonmous class creation
*/
public abstract class SkeletonGeodeticSystemConvertor implements GeodeticSystemConvertor {
    private GeodeticSystem convertsFrom, convertsTo;
    private int weight;

    /**
    * A constructor which creates a SkeletonGeodeticSystemConvertor with the Default weighing
    * @param conversionLinks The collection of GeodeticSystemConvertors in the order which conversion should occur
    */
    public SkeletonGeodeticSystemConvertor(GeodeticSystem convertsFrom, GeodeticSystem convertsTo) {
	this(convertsFrom, convertsTo,DEFAULT_CONVERSION_WEIGHT);
    }

    /**
    * A constructor which creates a SkeletonGeodeticSystemConvertor with the Default weighing
    * @param conversionLinks The collection of GeodeticSystemConvertors in the order which conversion should occur
    */
    public SkeletonGeodeticSystemConvertor(GeodeticSystem convertsFrom, GeodeticSystem convertsTo, int weight) {
	this.convertsFrom = convertsFrom;
	this.convertsTo = convertsTo;
	this.weight = weight;
    }

    /**
    * Gets the resultant node which this GeodeticSystemConvertor converts to
    * @return The GeodeticSystem this Convertor can convert to
    */
    public final GeodeticSystem getEndingNode() {
	return convertsTo;
    }

    /**
    * Gets the GeodeticSystem which this Convertor converts from
    * @return The Geodetic system this convertor converts from
    */
    public final GeodeticSystem getStartingNode() {
	return convertsFrom;
    }

    /**
    * Gets the Link weight that is associated with this GeodeticSystemConvertor.
    * it should be noted that a high weight (when compared to other GeodeticSystemConvertors) will likely indicate a Convertor which is not ideal.
    * @return The linkweight of this GeodeticSystemConvertor
    */
    public final int getLinkWeight() {
	return weight;
    }

    /**
    * Converts the input coordinates to the end nodes GeodeticSystem form
    * @param input The input to convert to the end nodes GeodeticSystem form
    * @return the resultant convertion caried out by this abstract SkeletonGeodeticConvertor
    */
    public abstract double[] convert(double[] input);
}