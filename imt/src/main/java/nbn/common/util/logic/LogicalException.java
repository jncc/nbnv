package nbn.common.util.logic;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 16-Sep-2010
* @description	    :- This class represents a Logic Exception.
* A logical exception is the production equivalent of an AssertionError.
* The case is that an AssertionError should not be thrown in a production
* environment as it may have unforseen consequences on underlying technologies.
*/
public class LogicalException extends RuntimeException {
    public LogicalException() {
	super();
    }

    public LogicalException(String mess) {
	super(mess);
    }

    public LogicalException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public LogicalException(Throwable cause) {
	super(cause);
    }
}
