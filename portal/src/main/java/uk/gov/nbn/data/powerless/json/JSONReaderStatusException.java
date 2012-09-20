package uk.gov.nbn.data.powerless.json;

/**
 * The following expection will be thrown if the status from the remote
 * api is not 200
 * @author Christopher Johnson
 */
public class JSONReaderStatusException extends Exception {
    private final int statusCode;
    public JSONReaderStatusException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
