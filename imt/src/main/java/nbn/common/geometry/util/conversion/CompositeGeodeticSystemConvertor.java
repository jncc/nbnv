package nbn.common.geometry.util.conversion;

import java.util.Vector;
import java.util.Collection;
/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- This class is used to chain GeodeticSystemConverters together so that conversions can be carried out between GeodeticSystems which don't have a direct conversion method
*/
public class CompositeGeodeticSystemConvertor implements GeodeticSystemConvertor {
    private Vector<GeodeticSystemConvertor> conversionLinks;
    private int conversionWeight;

    /**
    * A constructor which creates a Composite Geodetic System convertor
    * @param conversionLinks The collection of GeodeticSystemConvertors in the order which conversion should occur
    */
    public CompositeGeodeticSystemConvertor(Collection<GeodeticSystemConvertor> conversionLinks) {
	if(conversionLinks == null || conversionLinks.isEmpty())
	    throw new IllegalArgumentException("A composite geodetic system convertor requires at least one conversion links");
	this.conversionLinks = new Vector<GeodeticSystemConvertor>(conversionLinks);
	conversionWeight = calculateConversionWeight(conversionLinks);
    }

    /**
    * Gets the GeodeticSystem which this Convertor converts from
    * @return The Geodetic system this convertor converts from
    */
    public final GeodeticSystem getStartingNode() {
	return conversionLinks.firstElement().getStartingNode(); //returnt the last converstion links convertsTo
    }

    /**
    * Gets the resultant node which this GeodeticSystemConvertor converts to
    * @return The GeodeticSystem this Convertor can convert to
    */
    public final GeodeticSystem getEndingNode() {
	return conversionLinks.lastElement().getEndingNode(); //returnt the last converstion links convertsTo
    }

    /**
    * Gets the Link weight that is associated with this GeodeticSystemConvertor.
    * it should be noted that a high weight (when compared to other CompositeGeodeticSystemConvertors) will likely indicate a Convertor which is not ideal.
    * @return The total linkweight of all of the underlying GeodeticSystemConvertors which this CompositeGeodeticSystemConvertor is built of
    */
    public final int getLinkWeight() {
	return conversionWeight;
    }

    /**
    * Converts the input coordinates to the end nodes GeodeticSystem form
    * @param input The input (x,y,z) to convert to the end nodes GeodeticSystem form
    * @return the resultant (x,y,z) convertion caried out by this CompositeGeodeticConvertor
    */
    public double[] convert(double[] input) {
	double[] toReturn = input;
	for(GeodeticSystemConvertor currConversion : conversionLinks) //go through each conversion and convert
	    toReturn = currConversion.convert(toReturn);
	return toReturn;
    }

    private int calculateConversionWeight(Collection<GeodeticSystemConvertor> conversionLinks) {
	conversionWeight = 0;
	for(GeodeticSystemConvertor currConvertor : conversionLinks) //calcuate the conversion weight
	    conversionWeight += currConvertor.getLinkWeight(); //do a quick sumation
	return conversionWeight;
    }
}