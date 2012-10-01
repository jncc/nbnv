package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The following annotation represents that this object is capable of intercepting
 * map requests and modifying them.
 * 
 * @see Intercepts
 * @author Christopher Johnson
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercepts {
    MapServiceMethod.Type[] value();
}
