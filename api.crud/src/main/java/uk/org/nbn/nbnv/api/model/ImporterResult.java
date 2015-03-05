package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImporterResult {
    private List<ValidationError> validationErrors;
    private String timestamp;
    private boolean success;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public ImporterResult(List<ValidationError> validationErrors, boolean success, String timestamp) {
        this.validationErrors = validationErrors;
        this.success = success;
        this.timestamp = timestamp;
    }
    
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
