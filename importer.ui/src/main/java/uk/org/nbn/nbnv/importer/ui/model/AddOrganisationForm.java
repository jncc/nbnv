/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

import java.util.ArrayList;
import java.util.List;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;


/**
 * Container to store errors / new organisation input results
 * 
 * @author Matt Debont
 */
public class AddOrganisationForm {
    private Organisation organisation;
    private List<String> errors;
    private Boolean allowPub = false;

    public Boolean getAllowPub() {
        return allowPub;
    }

    public void setAllowPub(Boolean allowPub) {
        this.allowPub = allowPub;
    }

    public AddOrganisationForm () {
        this.organisation = new Organisation();
        
        this.organisation.setAbbreviation("");
        this.organisation.setAddress("");
        this.organisation.setAllowPublicRegistration(false);
        this.organisation.setContactEmail("");
        this.organisation.setContactName("");
        this.organisation.setLogo("");
        this.organisation.setLogoSmall("");
        this.organisation.setOrganisationName("");
        this.organisation.setPhone("");
        this.organisation.setPostcode("");
        this.organisation.setSummary("");
        this.organisation.setWebsite("");         
        
//        this.organisation.setAbbreviation("TEST");
//        this.organisation.setAddress("123 Testing Drive\nTestington");
//        this.organisation.setAllowPublicRegistration(true);
//        this.organisation.setContactEmail("test@test.com");
//        this.organisation.setContactName("Testy McTestington");
//        this.organisation.setLogoURL("LogoURL");
//        this.organisation.setOrganisationID(-1);
//        this.organisation.setOrganisationName("Testing Event Strategy Testimonials");
//        this.organisation.setPhone("01648599775");
//        this.organisation.setPostcode("PE1 1SD");
//        this.organisation.setSummary("Test Summary");
//        this.organisation.setWebsite("www.test.com");     
        
        this.errors = new ArrayList<String>();
    }
    
    public Organisation getOrganisation() {
        return this.organisation;
    }
    
    public void setOrgagnisation(Organisation organisation) {
        this.organisation = organisation;
    }

    /**
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public void addErrors(List<String> errors) {
        for (String error : errors) {
            addError(error);
        }
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
}
