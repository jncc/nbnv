/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.portal.exceptions;

/**
 *
 * @author Matt Debont
 */
public class InvalidFeatureIdentifierException extends Exception {
    private String identifier;
    
    public InvalidFeatureIdentifierException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }
    
    public InvalidFeatureIdentifierException(String message) {
        super(message);
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return identifier;
    }
}
