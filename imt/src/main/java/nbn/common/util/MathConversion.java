
package nbn.common.util;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 17-Sep-2010
* @description	    :-
*/
public class MathConversion {
    public static double getCoveragePercentage(double segmentArea, double totalArea) {
	return Math.min(getPercentage(segmentArea,totalArea),100d);
    }

    public static double getPercentage(double segmentArea, double totalArea) {
        return 100 * segmentArea / totalArea;
    }

    public static double convertMetresToHectares(double sqMetres) {
        return sqMetres * 0.0001;
    }
}
