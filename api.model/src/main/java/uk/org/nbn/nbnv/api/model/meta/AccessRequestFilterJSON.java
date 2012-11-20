/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author Paul Gilbertson
 */
public class AccessRequestFilterJSON {
    private String type;
    
    // Year filter
    private int start;
    private int end;
    
    // Taxon filter
    private String taxon;
    private String sciname;
    
    // Spatial filter
    private String match;
    private String boundary;
    private String boundaryType;
    private String feature;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the taxon
     */
    public String getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the sciName
     */
    public String getSciname() {
        return sciname;
    }

    /**
     * @param sciName the sciName to set
     */
    public void setSciname(String sciname) {
        this.sciname = sciname;
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return the boundary
     */
    public String getBoundary() {
        return boundary;
    }

    /**
     * @param boundary the boundary to set
     */
    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    /**
     * @return the boundaryType
     */
    public String getBoundaryType() {
        return boundaryType;
    }

    /**
     * @param boundaryType the boundaryType to set
     */
    public void setBoundaryType(String boundaryType) {
        this.boundaryType = boundaryType;
    }

    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }
            
}
