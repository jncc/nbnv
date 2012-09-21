package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotation which registers a map service to a given path
 * @author Christopher Johnson
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MapObject {
    String map();
    String path() default "";  //The path from the MapService to register this 
                                //method to. Default is to register to the 
                                //map services path
}
