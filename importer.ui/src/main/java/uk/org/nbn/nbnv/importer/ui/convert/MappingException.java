/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

/**
 *
 * @author Matt Debont
 */
public class MappingException extends BadDataException {
    
    /**
     * Creates a new instance of
     * <code>MappingException</code> without detail message.
     */
    public MappingException() {
    }

    /**
     * Constructs an instance of
     * <code>MappingException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MappingException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>MappingException</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public MappingException(Exception ex) {
        super(ex);
    }    
}
