/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.validators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
public class OrganisationValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Organisation.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Organisation org = (Organisation) o;
        Pattern pattern;
        
        // Organisation Name Validators
        if (org.getName().trim().isEmpty()) {
            // Required
            errors.rejectValue("name", "organisationName.required");
        } else {
            // Check Organisation Name does not exist already
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Organisation.findByName");
            q.setParameter("name", org.getName());

            if (!q.getResultList().isEmpty()) {
                errors.rejectValue("name", "organisationName.exists");
            }
            em.close();
        }
        
        // Abbreviation Validators
        // None required, optional input parameter
        
        // Summary Validator
        // Required
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "summary.required");
        
        // Name Validators
        // Required
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName", "contactName.required");
        
        // Email Validators
        // Required, should match a general pattern of an email address
        if (org.getContactEmail().trim().isEmpty()) {
            errors.rejectValue("contactEmail", "contactEmail.required");
        } else {
            pattern = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
            if (!pattern.matcher(org.getContactEmail()).matches()) {
                errors.rejectValue("contactEmail", "contactEmail.invalid");
            }
        }
        
        // Address Validators
        // TODO: Required as per Metada Form / Paul concerned about DPA so left as optional
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "address.notFound");
        
        // UK Postcode Validator
        // Required 
        // TODO: Broken
        //pattern = Pattern.compile("^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)$");
        //if (!pattern.matcher(org.getPostcode()).matches()) {
        //     errors.rejectValue("postcode", "postcode.invalid");
        //}        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "postcode", "postcode.notFound");
        
        // Phone Number Validator
        // Required
        if (org.getPhone().trim().equals("")) {
            errors.rejectValue("phone", "phone.required");
        } else {
            pattern = Pattern.compile("^([\\+][0-9]{1,3}([ \\.\\-])?)?([\\(]{1}[0-9]{3}[\\)])?([0-9 \\.\\-]{1,32})((x|ext|extension)?[0-9]{1,4}?)$");
            if (!pattern.matcher(org.getPhone()).matches()) {
                errors.rejectValue("phone", "phone.invalid");
            }        
        }
        
        // Website Validator
        // Not required, but if exists we should validate
        if (!org.getWebsite().trim().equals("")) {
            try {
                String uri = org.getWebsite();
                if (!(uri.startsWith("http://") || uri.startsWith("https://"))) {
                    uri = "http://" + uri;
                } 
                URL url = new URL(uri);
            } catch (MalformedURLException ex) {
                errors.rejectValue("website", "website.invalid");
            }
        }
        
        // Logo Validator
        // None required, optional input parameter
        
        // Allow Public Access Validators
        // None Needed - Set to false by default
    }
    
}