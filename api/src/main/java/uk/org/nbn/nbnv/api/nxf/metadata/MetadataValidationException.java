package uk.org.nbn.nbnv.api.nxf.metadata;

import java.util.List;

public class MetadataValidationException extends Exception{
    
    private List<String> errors;
    
    public MetadataValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public MetadataValidationException(String message) {
        super(message);
    }

    public MetadataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetadataValidationException(Throwable cause) {
        super(cause);
    }
    
    public List<String> getErrors(){
        return errors;
    }
}
