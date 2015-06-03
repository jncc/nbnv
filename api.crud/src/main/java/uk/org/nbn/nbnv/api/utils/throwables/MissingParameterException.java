/*
 */
package uk.org.nbn.nbnv.api.utils.throwables;

/**
 *
 * @author Matt Debont
 */
public class MissingParameterException extends RuntimeException {
    public MissingParameterException() {
        super();
    }

    public MissingParameterException(String message) {
        super(message);
    }

    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingParameterException(Throwable cause) {
        super(cause);
    }

    protected MissingParameterException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }	
}
