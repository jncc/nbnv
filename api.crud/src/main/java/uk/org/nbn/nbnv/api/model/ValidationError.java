package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValidationError {
    private String rule, message, recordKey;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }
}
