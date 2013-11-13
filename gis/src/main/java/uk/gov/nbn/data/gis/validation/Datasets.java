package uk.gov.nbn.data.gis.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Christopher Johnson
 */
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatasetsConstraintValidator.class)
@Documented
public @interface Datasets {
    
    String message() default "{uk.gov.nbn.data.gis.validation.Datasets.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
