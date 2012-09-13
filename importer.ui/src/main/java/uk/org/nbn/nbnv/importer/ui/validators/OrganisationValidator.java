/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.validators;

import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
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
        // Organisation Name Validators
        ValidationUtils.rejectIfEmpty(errors, "organisationName", "name.empty");        
        
        Organisation org = (Organisation) o;
        
        // Check Organisation Name does not exist already
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("Organisation.findByOrganisationName");
        q.setParameter("organisationName", org.getOrganisationName());
        
        if (!q.getResultList().isEmpty()) {
            errors.rejectValue("organisationName", "name.exists");
        }
        
        // Abbreviation Validators
        // None at present
        
        // Address Validators
        // None at present
        
        // Allow Public Access Validators
        // None Needed
        
        // Email Validators
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
        if (!pattern.matcher(org.getContactEmail()).matches()) {
            errors.rejectValue("contactEmail", "email.invalid");
        }
        
        // Name Validators
        // None Needed
        
        // Logo Validator
        // None Needed
        
        // Phone Number Validator
        pattern = Pattern.compile("^[0-9]{0,14}$");
        if (!pattern.matcher(org.getPhone()).matches()) {
            errors.rejectValue("phone", "phone.invalid");
        }
        
        // UK Postcode Validator
        // TODO: Broken
        //pattern = Pattern.compile("^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)$");
        //if (!pattern.matcher(org.getPostcode()).matches()) {
        //     errors.rejectValue("postcode", "postcode.invalid");
        //}
        
        // Summary Validator
        // None Needed
        
        // Website Validator
        // Probably need, but uncertain if we actually do
    }
    
}
