package uk.org.nbn.nbnv.api.rest.resources;

public abstract class ObservationResource extends AbstractResource {

    public static final String SPATIAL_RELATIONSHIP_WITHIN = "within";
    public static final String SPATIAL_RELATIONSHIP_OVERLAP = "overlap";
    public static final String SPATIAL_RELATIONSHIP_DEFAULT = SPATIAL_RELATIONSHIP_OVERLAP;

    protected final String defaultStartYear = "-1";
    protected final String defaultEndYear = "-1";
    protected final String defaultDatasetKey = "";
    protected final String defaultTaxa = "";
    protected final String defaultSensitive = "1";
    protected final String defaultDesignation = "";
    protected final String defaultTaxonOutputGroup = "";
    protected final String defaultGridRef = "";
    protected final String defaultFeatureID = "";
    protected final String defaultAttributeID = "-1";

}
