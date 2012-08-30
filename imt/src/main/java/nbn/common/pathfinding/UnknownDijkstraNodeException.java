package nbn.common.pathfinding;
/**
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :-
*/
public class UnknownDijkstraNodeException extends Exception {
    public UnknownDijkstraNodeException() {
	super();
    }

    public UnknownDijkstraNodeException(String mess) {
	super(mess);
    }

    public UnknownDijkstraNodeException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public UnknownDijkstraNodeException(Throwable cause) {
	super(cause);
    }
}