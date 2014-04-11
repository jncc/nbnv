/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.revoker.model;

import javax.xml.bind.annotation.XmlRootElement;
import uk.org.nbn.nbnv.api.model.*;

/**
 *
 * @author Matt Debont
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
