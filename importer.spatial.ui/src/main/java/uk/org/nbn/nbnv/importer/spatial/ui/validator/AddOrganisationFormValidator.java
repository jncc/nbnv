/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.spatial.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
public class AddOrganisationFormValidator implements Validator {

    private final Validator organisationValidator;
    
    public AddOrganisationFormValidator(Validator organisationValidator) {
        if (organisationValidator == null) {
            throw new IllegalArgumentException("The supplied validator is required and must not be null");
        }
        if (!organisationValidator.supports(Organisation.class)) {
            throw new IllegalArgumentException("The supplied validator must support the validation of Organisation instances");
        }
        this.organisationValidator = organisationValidator;
    }
    
    @Override
    public boolean supports(Class<?> type) {
        return AddOrganisationForm.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddOrganisationForm form = (AddOrganisationForm) o;
        
        try {
            errors.pushNestedPath("organisation");
            ValidationUtils.invokeValidator(this.organisationValidator, form.getOrganisation(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
    
}
