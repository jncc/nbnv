package nbn.common.pathfinding;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :-
*/
public class DijkstraNodeAlreadyKnownException extends Exception {
    public DijkstraNodeAlreadyKnownException() {
	super();
    }

    public DijkstraNodeAlreadyKnownException(String mess) {
	super(mess);
    }

    public DijkstraNodeAlreadyKnownException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public DijkstraNodeAlreadyKnownException(Throwable cause) {
	super(cause);
    }
}