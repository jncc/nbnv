/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.params;

/**
 *
 * @author Administrator
 */
public class ParameterNormalisationException extends Exception {
    public ParameterNormalisationException() {
        super();
    }

    public ParameterNormalisationException(String mess) {
        super(mess);
    }

    public ParameterNormalisationException(String mess, Throwable cause) {
        super(mess,cause);
    }

    public ParameterNormalisationException(Throwable cause) {
        super(cause);
    }
}
