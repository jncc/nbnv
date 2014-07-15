package uk.org.nbn.nbnv.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.org.nbn.nbnv.Logger;
import uk.org.nbn.nbnv.domain.MetadataForm;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * include javadoc
 */
public class MetadataFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MetadataForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(MetadataForm.class).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyDescriptorName = propertyDescriptor.getName();
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, propertyDescriptorName, propertyDescriptorName + " can not be empty.");
                // wil be displayed to user, via exception handler base controller
            }
        } catch (IntrospectionException e) {
            errors.reject("Exception occurred");
            Logger.error(e.getMessage());
        }


    }
}
