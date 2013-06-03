/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author paulbe
 */
public interface OperationalOrganisationTaxonObservationAccessMapper {
    @Insert("INSERT INTO OrganisationTaxonObservationAccess (organisationID, observationID) VALUES (#{organisationID}, #{observationID}")
    public void AddAccess(@Param("organisationID") int organisationID, @Param("observationID") int observationID);
    
    @Delete("DELETE FROM OrganisationTaxonObservationAccess WHERE organisationID = #{organisationID} AND observationID = #{observationID}")
    public void RemoveAccess(@Param("organisationID") int organisationID, @Param("observationID") int observationID);
}
