/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.errors;

/**
 *
 * @author Matt Debont
 */
public class NotHarvestedError extends Error {
        /**
     * Creates a new instance of
     * <code>NotHarvestedError</code> without detail message.
     */
    public NotHarvestedError() {
    }

    /**
     * Constructs an instance of
     * <code>NotHarvestedError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NotHarvestedError(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>NotHarvestedError</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public NotHarvestedError(Exception ex) {
        super(ex);
    }    
}
