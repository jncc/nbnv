package uk.org.nbn.nbnv.api.nxf.metadata;

/**
 *
 * @author Matt Debont
 */
public class POIImportError extends Exception {
        
        /**
     * Creates a new instance of
     * <code>POIImportError</code> without detail message.
     */
    public POIImportError() {
    }

    /**
     * Constructs an instance of
     * <code>POIImportError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public POIImportError(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>POIImportError</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public POIImportError(Exception ex) {
        super(ex);
    }
}
