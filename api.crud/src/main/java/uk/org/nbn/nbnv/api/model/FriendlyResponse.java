package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public class FriendlyResponse {
    private boolean success;
    private String status;

    public FriendlyResponse() {}
    
    public FriendlyResponse(boolean success, String status) {
        this.success = success;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
}
