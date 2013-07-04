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
public interface OperationalUserTaxonObservationAccessMapper {
    @Insert("INSERT INTO UserTaxonObservationAccess (userID, observationID) VALUES (#{userID}, #{observationID})")
    public void AddAccess(@Param("userID") int userID, @Param("observationID") int observationID);
    
    @Delete("DELETE FROM UserTaxonObservationAccess WHERE userID = #{userID} AND observationID = #{observationID}")
    public void RemoveAccess(@Param("userID") int userID, @Param("observationID") int observationID);
}
