/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.errors;

/**
 *
 * @author Matt Debont
 */
public class ImportWarningException extends Exception {
    
    /**
     * Creates a new instance of
     * <code>ImportWarningException</code> without detail message.
     */
    public ImportWarningException() {
    }

    /**
     * Constructs an instance of
     * <code>ImportWarningException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ImportWarningException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>ImportWarningException</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public ImportWarningException(Exception ex) {
        super(ex);
    }
}
