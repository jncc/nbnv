
package nbn.common.feature;

public enum FeatureType {
    GRIDSQUARE("GRID"), SITEBOUNDARY("SITEBOUNDARY");
    
    private String type;
    private FeatureType(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type;
    }

    public static FeatureType getFeatureType(String typeAsStr) {
        for(FeatureType currFeatureType: values()) {
            if(typeAsStr.equals(currFeatureType.type))
                return currFeatureType;
        }
        throw new IllegalArgumentException("There is no FeatureType which is represented by " + typeAsStr);
    }
}
