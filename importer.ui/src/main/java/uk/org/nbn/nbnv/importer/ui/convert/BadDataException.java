/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

/**
 *
 * @author Paul Gilbertson
 */
public class BadDataException extends Exception {

    /**
     * Creates a new instance of
     * <code>BadDataException</code> without detail message.
     */
    public BadDataException() {
    }

    /**
     * Constructs an instance of
     * <code>BadDataException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BadDataException(String msg) {
        super(msg);
    }
}
