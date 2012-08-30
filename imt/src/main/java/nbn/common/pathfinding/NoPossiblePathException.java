package nbn.common.pathfinding;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :-
*/
public class NoPossiblePathException extends Exception {
    public NoPossiblePathException() {
	super();
    }

    public NoPossiblePathException(String mess) {
	super(mess);
    }

    public NoPossiblePathException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public NoPossiblePathException(Throwable cause) {
	super(cause);
    }
}