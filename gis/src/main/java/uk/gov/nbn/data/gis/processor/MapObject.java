package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Administrator
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MapObject {
    String value() default "";
}
