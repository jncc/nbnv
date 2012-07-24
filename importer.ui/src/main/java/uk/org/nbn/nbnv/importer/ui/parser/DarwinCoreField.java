/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.parser;

/**
 *
 * @author Administrator
 */
public enum DarwinCoreField {
    OCCURRENCEID ("http://rs.tdwg.org/dwc/terms/occurrenceID", DarwinCoreFieldType.DWCA_OCCURRENCE),
    EVENTDATE ("http://rs.tdwg.org/dwc/terms/eventDate", DarwinCoreFieldType.DWCA_OCCURRENCE),
    TAXONID ("http://rs.tdwg.org/dwc/terms/taxonID", DarwinCoreFieldType.DWCA_OCCURRENCE),
    LOCATIONID ("http://rs.tdwg.org/dwc/terms/locationID", DarwinCoreFieldType.DWCA_OCCURRENCE),
    LOCALITY ("http://rs.tdwg.org/dwc/terms/locality", DarwinCoreFieldType.DWCA_OCCURRENCE),
    VERBATIMLATITUDE ("http://rs.tdwg.org/dwc/terms/verbatimLatitude", DarwinCoreFieldType.DWCA_OCCURRENCE),
    VERBATIMLONGITUDE ("http://rs.tdwg.org/dwc/terms/verbatimLongitude", DarwinCoreFieldType.DWCA_OCCURRENCE),
    VERBATIMSRS ("http://rs.tdwg.org/dwc/terms/verbatimSRS", DarwinCoreFieldType.DWCA_OCCURRENCE),
    RECORDEDBY ("http://rs.tdwg.org/dwc/terms/recordedBy", DarwinCoreFieldType.DWCA_OCCURRENCE),
    IDENTIFIEDBY ("http://rs.tdwg.org/dwc/terms/identifiedBy", DarwinCoreFieldType.DWCA_OCCURRENCE),
    OCCURRENCESTATUS ("http://rs.tdwg.org/dwc/terms/occurrenceStatus", DarwinCoreFieldType.DWCA_OCCURRENCE),
    COLLECTIONCODE ("http://rs.tdwg.org/dwc/terms/collectionCode", DarwinCoreFieldType.DWCA_OCCURRENCE),
    EVENTID ("http://rs.tdwg.org/dwc/terms/eventID", DarwinCoreFieldType.DWCA_OCCURRENCE),
    EVENTDATETYPECODE ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/dateTypeCode", DarwinCoreFieldType.NBNEXCHANGE),
    GRIDREFERENCE ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference", DarwinCoreFieldType.NBNEXCHANGE),
    GRIDREFERENCETYPE ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType", DarwinCoreFieldType.NBNEXCHANGE),
    GRIDREFERENCEPRECISION ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecision", DarwinCoreFieldType.NBNEXCHANGE),
    SITEFEATUREKEY ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/siteFeatureKey", DarwinCoreFieldType.NBNEXCHANGE),
    SENSITIVEOCCURRENCE ("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence", DarwinCoreFieldType.NBNEXCHANGE);
    
    
    
    
    private final String term;
    private final DarwinCoreFieldType type;
    
    DarwinCoreField(String term, DarwinCoreFieldType type) {
        this.term = term;
        this.type = type;
    }
    
    public String getTerm() {
        return this.term;
    }
    
    @Override
    public String toString() {
        return this.term;
    }
}
