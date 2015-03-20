package uk.org.nbn.nbnv.api.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ImporterResult implements Comparable<ImporterResult> {
    private List<ValidationError> validationErrors;
    private String timestamp;
    private State state;
    
    public enum State {
        SUCCESSFUL, VALIDATION_ERRORS, BAD_FILE, MISSING_SENSITIVE_COLUMN
    }
    
    public ImporterResult() {}

    public ImporterResult(List<ValidationError> validationErrors, State state, String timestamp) {
        this.validationErrors = validationErrors;
        this.state = state;
        this.timestamp = timestamp;
    }
    
    public ImporterResult(State state, String timestamp) {
        this.timestamp = timestamp;
        this.state = state;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    @XmlTransient
    public Date getTime() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return formatter.parse(timestamp.substring(0, 17));
    }

    @Override
    public int compareTo(ImporterResult o) {
        return o.timestamp.compareTo(timestamp);
    }
}
