package uk.org.nbn.nbnv.api.nxf.metadata;

import java.util.List;
import org.apache.commons.lang.StringUtils;

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

    @Override
    public String getMessage(){
        return "Encountered these errors in the Word metadata document:\n" + StringUtils.join(errors, "\n");
    }
}