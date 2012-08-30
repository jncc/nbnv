
package nbn.common.feature;

import java.util.regex.Pattern;

public enum GridSystem {
    BRITISH_NATIONAL_GRID(Pattern.compile("[A-Za-z][A-Za-z][A-Za-z0-9]*"),"British National Grid", SRS.EPSG_27700),
    IRISH_NATIONAL_GRID(Pattern.compile("[A-Za-z][0-9][A-Za-z0-9]*"),"Irish National Grid", SRS.EPSG_29903);

    private Pattern regularEx;
    private String name;
    private SRS srs;

    private GridSystem(Pattern regularEx, String name, SRS srs) {
        this.regularEx = regularEx;
        this.name = name;
        this.srs = srs;
    }

    public String getName() {
        return name;
    }

    public SRS getRecommendedSpatialReferenceSystem() {
        return srs;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static GridSystem getGridSystemByGridRef(String gridRef) {
        for(GridSystem currSystem : values()) {
            if(currSystem.regularEx.matcher(gridRef).matches())
                return currSystem;
        }
        throw new IllegalArgumentException("Unable to determine a GridSystem for this grid ref");
    }
}
