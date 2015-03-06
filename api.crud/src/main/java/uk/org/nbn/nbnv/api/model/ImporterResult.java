package uk.org.nbn.nbnv.api.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ImporterResult {
    private List<ValidationError> validationErrors;
    private String timestamp;
    private boolean success;
    
    public ImporterResult() {}

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
    
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    @XmlTransient
    public Date getTime() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return formatter.parse(timestamp.substring(0, 17));
    }
}
