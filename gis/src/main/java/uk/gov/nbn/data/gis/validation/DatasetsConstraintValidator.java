package uk.gov.nbn.data.gis.validation;

import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A simple dataset keys validator for a list of strings
 * @author Christopher Johnson
 */
public class DatasetsConstraintValidator implements ConstraintValidator<Datasets, List<String>> {
    private Pattern datasetKeyPattern;
    
    @Override
    public void initialize(Datasets a) {
        this.datasetKeyPattern = Pattern.compile("^[A-Z0-9]{8}$");
    }

    @Override
    public boolean isValid(List<String> datasetKeys, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation(); //Disable the default message
        boolean valid = true;
        if(datasetKeys != null) { //only validate if not null
            for(String key : datasetKeys) {
                if(!datasetKeyPattern.matcher(key).matches()) {
                    //Just set the violation as the failed dataset key
                    context.buildConstraintViolationWithTemplate( key )
                            .addConstraintViolation();

                    valid = false;
                }
            }
        }
        return valid;
    }

}
