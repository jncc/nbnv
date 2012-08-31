package uk.gov.nbn.data.gis.providers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to be used in a map service to provide the real location
 * of a map file
 * @author Christopher Johnson
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MapFile {
    String value();
}
