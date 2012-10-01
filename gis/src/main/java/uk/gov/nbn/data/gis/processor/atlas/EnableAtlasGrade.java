package uk.gov.nbn.data.gis.processor.atlas;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotation which registers a map service method as being atlas grade
 * capable
 * @author Christopher Johnson
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAtlasGrade {
    String[] layers();
}

