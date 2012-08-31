package nbn.common.bridging;

public class BridgingException extends RuntimeException {
    public BridgingException() {
	super();
    }

    public BridgingException(String mess) {
	super(mess);
    }

    public BridgingException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public BridgingException(Throwable cause) {
	super(cause);
    }
}