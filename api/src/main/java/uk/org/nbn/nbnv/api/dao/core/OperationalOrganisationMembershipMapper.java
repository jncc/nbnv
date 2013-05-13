/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author Matt Debont
 */
public interface OperationalOrganisationMembershipMapper {
    
    @Update("UPDATE UserOrganisationMembership SET (organisationRoleID = {organistionRoleID}) WHERE userID = {userID} AND organisationID = {organisationID}")
    public int changeUserRole(@Param("organisationRoleID") int organisationRoleID, @Param("userID") int userID, @Param("organisationID") int organisationID);
    
    @Delete("DELETE FROM UserOrganisationMembership WHERE userID = {userID} AND organisationID = {organisationID}")
    public int removeUser(@Param("userID") int userID, @Param("organisationID") int organisationID);
}
