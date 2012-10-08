package uk.gov.nbn.data.gis.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be applied to Provider classes. By doing so, it shows that
 * the given provider makes use of some query parameters. 
 * 
 * If this provider was used in the calling of a MapService method then the 
 * ServiceURLProvider will take this into account when producing urls.
 * 
 * @see ServiceURLProvider
 * @author Christopher Johnson
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Utilises {
    QueryParam[] value();
}
