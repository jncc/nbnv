package nbn.common.taxon;

public enum Abundance {
    ALL("all", -1, "Record of any Abundance"),ABSENCE("absence", 0, "Absence Record"),PRESENCE("presence", 1, "Presence Record");
    private int observationValue;
    private String value, description;

    private Abundance(String value, int observationValue, String description) {
        this.value = value;
        this.observationValue = observationValue;
        this.description = description;
    }

    public int getObservationValue() {
        if(this == ALL)
            throw new IllegalStateException("The Abundance type of All does not have a observation value");
        return observationValue;
    }

    public static Abundance getAbundanceFromValue(String value) {
        if(value==null) return ALL;
        for(Abundance currAbundance : values()) {
            if(currAbundance.value.equals(value))
                return currAbundance;
        }
        throw new IllegalArgumentException("There is no abundance which matches "  + value);
    }

    public String getDescription() {
        return description;
    }

    public String getParameterValue() {
        return value;
    }
}