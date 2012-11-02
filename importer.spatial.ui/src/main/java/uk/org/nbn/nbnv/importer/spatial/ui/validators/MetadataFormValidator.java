/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.importer.spatial.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.spatial.ui.model.MetadataForm;

/**
 *
 * @author Matt Debont
 */
public class MetadataFormValidator implements Validator {

    private MetadataValidator metadataValidator;

    public MetadataFormValidator(MetadataValidator metadataValidator) {
        if (metadataValidator == null) {
            throw new IllegalArgumentException("The supplied validator is required and must not be null");
        }
        if (!metadataValidator.supports(Metadata.class)) {
            throw new IllegalArgumentException("The supplied validator must support the validation of Metadata instances");
        }
        this.metadataValidator = metadataValidator;
    }
    
    @Override
    public boolean supports(Class<?> type) {
        return MetadataForm.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MetadataForm form = (MetadataForm) o;
        
        try {
            errors.pushNestedPath("metadata");
            ValidationUtils.invokeValidator(this.metadataValidator, form.getMetadata(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
    
}
