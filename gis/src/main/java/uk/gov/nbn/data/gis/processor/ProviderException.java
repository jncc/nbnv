package uk.gov.nbn.data.gis.processor;

/**
 * Thrown if there was an exception in a provider
 * @author Chris Johnson
 */
public class ProviderException extends Exception {
    public ProviderException(String msg) {
        super(msg);
    }

    public ProviderException(String mess, Throwable cause) {
        super(mess, cause);
    }
}
