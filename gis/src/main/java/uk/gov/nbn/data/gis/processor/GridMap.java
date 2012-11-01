package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotation which registers a map service method as being grid map
 * capable
 * @author Christopher Johnson
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GridMap {
    static class Resolution {
        public static final int TEN_KM = 10000, TWO_KM = 2000, ONE_KM=1000, ONE_HUNDRED_METERS = 100;
    }
    
    @interface GridLayer {
        String name();
        String layer();
        int resolution();
    }
    
    @interface Layer {
        String name();
        String[] layers();
    }

    GridLayer[] layers();
    String defaultLayer();
    Layer[] backgrounds() default {
        @Layer(name="os", layers="OS-Scale-Dependent" ),
        @Layer(name="gb", layers="GB-Coast" ),
        @Layer(name="gbi", layers={"GB-Coast", "Ireland-Coast"} ),
        @Layer(name="gb100kgrid", layers="GB-Coast-with-Hundred-km-Grid" ),
        @Layer(name="gbi100kgrid", layers={"GB-Coast-with-Hundred-km-Grid", "Ireland-coast-with-Hundred-km-Grid"} ),
        @Layer(name="vicecounty", layers="Vice-counties" ),
        @Layer(name="gb100kextent", layers="GB-Hundred-km-Grid" ),
        @Layer(name="gb10kextent", layers="GB-Ten-km-Grid" ),
        @Layer(name="i100kextent", layers="Ireland-Hundred-km-Grid" ),
        @Layer(name="i10kextent", layers="Ireland-Ten-km-Grid" ),
        @Layer(name="gbi10kextent", layers={"GB-Ten-km-Grid-Ireland-cutout","Ireland-Ten-km-Grid-GB-cutout"} ),
        @Layer(name="gbi100kextent", layers={"GB-Hundred-km-Grid-Ireland-cutout","Ireland-Hundred-km-Grid-GB-cutout"} )
    };
    Layer[] overlays() default {};
    String[] defaultBackgrounds() default {"gbi100kgrid"};
    String[] defaultOverlays() default {};
    String epsgCode() default "EPSG:27700";
    int[] defaultExtent() default {-250000, -50000, 750000, 1300000};
}

