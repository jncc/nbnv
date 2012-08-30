
package nbn.common.geometry.reference;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 07-Sep-2010
* @description	    :-
*/
public class GeodeticSystemCannotBeConvertedToSeCoordinateReferenceException extends Exception {

    public GeodeticSystemCannotBeConvertedToSeCoordinateReferenceException() {
	super();
    }

    public GeodeticSystemCannotBeConvertedToSeCoordinateReferenceException(String mess) {
	super(mess);
    }

    public GeodeticSystemCannotBeConvertedToSeCoordinateReferenceException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public GeodeticSystemCannotBeConvertedToSeCoordinateReferenceException(Throwable cause) {
	super(cause);
}
}
