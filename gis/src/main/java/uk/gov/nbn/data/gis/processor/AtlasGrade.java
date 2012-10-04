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
    enum Resolution {
        TENKM("10km", 10000), TWOKM("2km", 2000),ONEKM("1km", 1000),ONEHUNDERD("100m", 100);
        private String paramValue;
        private int metres;
        
        private Resolution(String paramValue, int metres) {
            this.paramValue = paramValue;
            this.metres = metres;
        }
        
        public int getResolutionInMetres() {
            return metres;
        }
        
        public static Resolution getResolutionFromParamValue(String paramValue) {
            for(Resolution currRes : values()) {
                if(currRes.paramValue.equals(paramValue)) {
                    return currRes;
                }
            }
            throw new IllegalArgumentException("There is no resolution which relates to " + paramValue);
        }
    }
    
    @interface Layer {
        String layer();
        Resolution resolution();
    }

    Layer[] layers();
    String[] backgrounds() default {};
    String[] defaultBackgrounds() default {};
    String epsgCode() default "EPSG:27700";
    int[] defaultExtent() default {-250000, -50000, 750000, 1300000};
    Resolution defaultResolution() default Resolution.TENKM;
}

