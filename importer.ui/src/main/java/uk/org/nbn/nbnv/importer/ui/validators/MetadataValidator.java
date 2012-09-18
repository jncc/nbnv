/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;

/**
 *
 * @author Matt Debont
 */
public class MetadataValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Metadata.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "methods", "methods.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "purpose", "purpose.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quality", "quality.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "access", "access.required");
    }
}
