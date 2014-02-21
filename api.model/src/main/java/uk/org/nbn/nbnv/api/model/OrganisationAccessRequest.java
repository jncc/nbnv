/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
public class OrganisationAccessRequest extends AccessRequest {
    private Organisation organisation;

    /**
     * @return the organisation
     */
    public Organisation getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
