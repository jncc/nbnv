package uk.gov.nbn.data.gis.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the value of a particular param. Validated against a given 
 * validation regex in a MapServiceMethod
 * @author Christopher Johnson
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    public static final String NO_VALIDATION = "";

    String key();
    String validation() default NO_VALIDATION;
}
