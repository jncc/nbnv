/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Paul Gilbertson
 */
public class Organisation {
    private int organisationID;
    private String name;
    
    public Organisation() { }
    
    public Organisation(int organisationID, String name) {
        super();
        this.organisationID = organisationID;
        this.name = name;
    }

    /**
     * @return the organisationID
     */
    public int getOrganisationID() {
        return organisationID;
    }

    /**
     * @param organisationID the organisationID to set
     */
    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
