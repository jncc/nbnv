/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.errors;

/**
 *
 * @author Matt Debont
 */
public class AmbiguousDataException extends BadDataException {
    
    /**
     * Creates a new instance of
     * <code>AmbiguousDataException</code> without detail message.
     */
    public AmbiguousDataException() {
    }

    /**
     * Constructs an instance of
     * <code>AmbiguousDataException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AmbiguousDataException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>AmbiguousDataException</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public AmbiguousDataException(Exception ex) {
        super(ex);
    }    
    
}
