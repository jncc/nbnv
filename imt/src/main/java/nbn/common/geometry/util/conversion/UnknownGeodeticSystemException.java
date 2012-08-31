package nbn.common.geometry.util.conversion;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :-
*/
public class UnknownGeodeticSystemException extends Exception {
    public UnknownGeodeticSystemException() {
	super();
    }

    public UnknownGeodeticSystemException(String mess) {
	super(mess);
    }

    public UnknownGeodeticSystemException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public UnknownGeodeticSystemException(Throwable cause) {
	super(cause);
    }
}