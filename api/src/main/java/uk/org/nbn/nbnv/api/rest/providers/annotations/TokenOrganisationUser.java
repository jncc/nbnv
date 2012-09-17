package uk.org.nbn.nbnv.api.rest.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import uk.org.nbn.nbnv.api.model.OrganisationMembership.Role;

/**
 * Annotation which is to be used in jersey methods to obtain a user who is a
 * member of some organisation
 * @author Chris Johnson
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenOrganisationUser {
    String path() default "organisationID";
    Role[] roles();
}
