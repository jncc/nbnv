package uk.org.nbn.nbnv.api.rest.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which is to be used in jersey methods to obtain a user who is 
 * logged in and has the System Administrator role
 * @author Chris Johnson
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenSystemAdministratorUser {}