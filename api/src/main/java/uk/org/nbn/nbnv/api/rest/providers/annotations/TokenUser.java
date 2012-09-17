package uk.org.nbn.nbnv.api.rest.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which is to be used in jersey methods to obtain a user
 * @author Chris Johnson
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenUser {
    boolean allowPublic() default true;
    boolean suppressTokenExpiration() default true;
    boolean suppressInvalidToken() default true;
}
