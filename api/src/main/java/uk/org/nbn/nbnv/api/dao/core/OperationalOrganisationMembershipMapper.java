/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author Matt Debont
 */
public interface OperationalOrganisationMembershipMapper {
    
    @Insert("INSERT INTO UserOrganisationMembership VALUES (#{userID}, #{organisationID}, 1)")
    public int addUser(@Param("userID") int userID, @Param("organisationID") int organisationID);
    
    @Update("UPDATE UserOrganisationMembership SET organisationRoleID = #{organisationRoleID} WHERE userID = #{userID} AND organisationID = #{organisationID}")
    public int changeUserRole(@Param("organisationRoleID") int organisationRoleID, @Param("userID") int userID, @Param("organisationID") int organisationID);
    
    @Delete("DELETE FROM UserOrganisationMembership WHERE userID = #{userID} AND organisationID = #{organisationID}")
    public int removeUser(@Param("userID") int userID, @Param("organisationID") int organisationID);
}
