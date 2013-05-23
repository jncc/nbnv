/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Matt Debont
 */
public class EmailSettingsModel {

    private boolean allowEmailAlerts;
    private boolean subscribedToAdminEmails;
    private boolean subscribedToNBNMarketting;

    public EmailSettingsModel() {

    }
    
    public EmailSettingsModel(boolean allowEmailAlerts, boolean subscribedToAdminEmails, boolean subscribedToNBNMarketting) {
        this.allowEmailAlerts = allowEmailAlerts;
        this.subscribedToAdminEmails = subscribedToAdminEmails;
        this.subscribedToNBNMarketting = subscribedToNBNMarketting;
    }
    
    public boolean isAllowEmailAlerts() {
        return allowEmailAlerts;
    }

    public void setAllowEmailAlerts(boolean allowEmailAlerts) {
        this.allowEmailAlerts = allowEmailAlerts;
    }

    public boolean isSubscribedToAdminEmails() {
        return subscribedToAdminEmails;
    }

    public void setSubscribedToAdminEmails(boolean subscribedToAdminEmails) {
        this.subscribedToAdminEmails = subscribedToAdminEmails;
    }

    public boolean isSubscribedToNBNMarketting() {
        return subscribedToNBNMarketting;
    }

    public void setSubscribedToNBNMarketting(boolean subscribedToNBNMarketting) {
        this.subscribedToNBNMarketting = subscribedToNBNMarketting;
    }
}
