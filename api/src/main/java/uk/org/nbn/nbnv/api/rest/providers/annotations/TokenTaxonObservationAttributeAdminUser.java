package uk.org.nbn.nbnv.api.rest.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Matt Debont
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenTaxonObservationAttributeAdminUser {
    String dataset() default "datasetID";
    String attribute() default "attribute";
}
