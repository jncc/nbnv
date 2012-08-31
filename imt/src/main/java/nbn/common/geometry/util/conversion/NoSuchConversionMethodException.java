package nbn.common.geometry.util.conversion;

public class NoSuchConversionMethodException extends Exception {
    public NoSuchConversionMethodException() {
	super();
    }

    public NoSuchConversionMethodException(String mess) {
	super(mess);
    }

    public NoSuchConversionMethodException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public NoSuchConversionMethodException(Throwable cause) {
	super(cause);
    }
}