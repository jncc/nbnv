package uk.gov.nbn.data.gis.processor;

/**
 * An exception which is thrown if a particular map service does not exist
 * @author Christopher Johnson
 */
public class MapServiceUndefinedException extends Exception {
    public MapServiceUndefinedException(String mess) {
        super(mess);
    }    
}
