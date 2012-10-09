package uk.gov.nbn.data.gis.processor;

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
public @interface AtlasGrade {
    static class Resolution {
        public static final int TEN_KM = 10000, TWO_KM = 2000, ONE_KM=1000, ONE_HUNDRED_METERS = 100;
    }
    
    @interface GridLayer {
        String name();
        String layer();
        int snapToResolution();
        int lcmResolution();
    }
    
    @interface Layer {
        String name();
        String layer();
    }

    GridLayer[] layers();
    String defaultLayer();
    Layer[] backgrounds() default {};
    Layer[] overlays() default {};
    String[] defaultBackgrounds() default {};
    String[] defaultOverlays() default {};
    String epsgCode() default "EPSG:27700";
    int[] defaultExtent() default {-250000, -50000, 750000, 1300000};
}

