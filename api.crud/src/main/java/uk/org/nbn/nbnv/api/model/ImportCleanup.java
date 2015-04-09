package uk.org.nbn.nbnv.api.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportCleanup {
    @NotNull
    private Operation operation;

    public ImportCleanup() {}
    
    public ImportCleanup(Operation operation) {
        this.operation = operation;
    }
    
    public enum Operation {
        STRIP_INVALID_RECORDS, SET_SENSITIVE_TRUE, SET_SENSITIVE_FALSE
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
