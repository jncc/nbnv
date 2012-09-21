/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.util;

/**
 *
 * @author Matt Debont
 */
public class UnsatisfiableDependencyError extends Exception {
    
        /**
     * Creates a new instance of
     * <code>UnsatisfiableDependencyError</code> without detail message.
     */
    public UnsatisfiableDependencyError() {
    }

    /**
     * Constructs an instance of
     * <code>UnsatisfiableDependencyError</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnsatisfiableDependencyError(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of
     * <code>UnsatisfiableDependencyError</code> with the specified inner exception.
     *
     * @param ex the inner exception.
     */
    public UnsatisfiableDependencyError(Exception ex) {
        super(ex);
    }
    
}
