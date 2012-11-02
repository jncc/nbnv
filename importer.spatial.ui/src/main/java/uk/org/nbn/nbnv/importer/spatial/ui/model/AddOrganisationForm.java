/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;


/**
 * Container to store errors / new organisation input results
 * 
 * @author Matt Debont
 */
public class AddOrganisationForm {
    private Organisation organisation;
    private List<String> errors;
    private String imageError = "";
    private CommonsMultipartFile imageData;
    private String logo = "";
    private String logoSmall = "";

    public AddOrganisationForm () {
        this.organisation = new Organisation();
        
        this.organisation.setAbbreviation("");
        this.organisation.setAddress("");
        this.organisation.setAllowPublicRegistration(false);
        this.organisation.setContactEmail("");
        this.organisation.setContactName("");
        this.organisation.setLogo(null);
        this.organisation.setLogoSmall(null);
        this.organisation.setName("");
        this.organisation.setPhone("");
        this.organisation.setPostcode("");
        this.organisation.setSummary("");
        this.organisation.setWebsite("");            
        
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
    
    public void addErrors(String[] errors) {
        for (String error : errors) {
            addError(error);
        }
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public CommonsMultipartFile getImage() {
        return imageData;
    }

    public void setImage(CommonsMultipartFile imageData) {
        this.imageData = imageData;
    }
    
    public String getImageError() {
        return imageError;
    }

    public void setImageError(String imageError) {
        this.imageError = imageError;
    }    

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
    }
}
