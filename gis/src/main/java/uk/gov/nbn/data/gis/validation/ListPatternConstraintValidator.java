package uk.gov.nbn.data.gis.validation;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Christopher Johnson
 */
public class ListPatternConstraintValidator implements ConstraintValidator<Datasets, List<String>> {

    @Override
    public void initialize(Datasets a) {
    }

    @Override
    public boolean isValid(List<String> t, ConstraintValidatorContext cvc) {
        for(String key : t) {
            if(!key.matches("^[A-Z0-9]{8}$")) {
                return false;
            }
        }
        return true;
    }

}
