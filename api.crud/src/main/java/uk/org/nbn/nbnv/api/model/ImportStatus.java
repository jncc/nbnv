package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportStatus {
    private List<ValidationError> validationErrors;
    private boolean success;
    
    public ImportStatus(List<ValidationError> validationErrors, boolean success) {
        this.validationErrors = validationErrors;
        this.success = success;
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
