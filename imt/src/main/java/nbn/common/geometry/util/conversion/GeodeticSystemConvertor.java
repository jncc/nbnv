package nbn.common.geometry.util.conversion;

import nbn.common.pathfinding.DijkstraLink;

/**
* @author       :- Christopher Johnson
* @Date         :- 23-Aug-2010
* @Description  :- The interface which defines a Geodetic System Convertor
*/
public interface GeodeticSystemConvertor extends DijkstraLink<GeodeticSystem> {
    /**
    * The default weighting a GeodeticSystemConvertor should take, if there is not prioritisation between convertors
    */
    public static final int DEFAULT_CONVERSION_WEIGHT = 1; //this is the default weight of a path, weights can be used from prioritisation. e.g. more acurate conversions could take president over less acurate ones
    /**
    * Converts the input coordinates to the end nodes GeodeticSystem form
    * @param input The input to convert to the end nodes GeodeticSystem form
    * @return the resultant conversion carried out by this GeodeticConvertor
    */
    public double[] convert(double[] input);
}