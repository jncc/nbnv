/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author Paul Gilbertson
 */
public class EditAccessRequestJSON {
    private AccessRequestJSON json;
    private String rawJSON = "";
    private String action = "";
    private String reason = "";
    private String expires = "";

    public AccessRequestJSON getJson() {
        return json;
    }

    public void setJson(AccessRequestJSON json) {
        this.json = json;
    }

    public String getRawJSON() {
        return rawJSON;
    }

    public void setRawJSON(String rawJSON) {
        this.rawJSON = rawJSON;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
