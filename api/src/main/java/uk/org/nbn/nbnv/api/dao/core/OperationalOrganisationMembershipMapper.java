/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

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

    @Select("SELECT * FROM UserOrganisationMembership WHERE userID = #{userKey}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getLimitedUserByID")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID"))           
    })
    List<OrganisationMembership> selectByUser(@Param("userKey") int userKey);

    @Select("SELECT uom.userID, uom.organisationID, uor.label AS roleType FROM UserOrganisationMembership uom JOIN UserOrganisationRole uor ON uom.organisationRoleID = uor.id WHERE uom.organisationID = #{organisationID}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getLimitedUserByID")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="role", column="roleType", javaType= OrganisationMembership.Role.class)
    })
    List<OrganisationMembership> selectByOrganisation(@Param("organisationID") int organisationID);

    @Select("SELECT uom.userID, uom.organisationID, uor.label AS roleType FROM UserOrganisationMembership uom JOIN UserOrganisationRole uor ON uom.organisationRoleID = uor.id WHERE uom.organisationID = #{organisationID} AND uor.id > 1")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getLimitedUserByID")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="role", column="roleType", javaType= OrganisationMembership.Role.class)
    })
    List<OrganisationMembership> selectAdminsByOrganisation(@Param("organisationID") int organisationID);

    @Select("SELECT uom.userID, uom.organisationID, uor.label AS roleType FROM UserOrganisationMembership uom JOIN UserOrganisationRole uor ON uom.organisationRoleID = uor.id WHERE uom.userID = #{userKey} AND uom.organisationID = #{organisationID}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getLimitedUserByID")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="role", column="roleType", javaType= OrganisationMembership.Role.class)
    })
    OrganisationMembership selectByUserAndOrganisation(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
    
    @Select("SELECT COUNT(*) FROM UserOrganisationMembership WHERE userID = #{userKey} AND organisationID = #{organisationID}")
    boolean isUserMemberOfOrganisation(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
    
    @Select("SELECT COUNT(*) FROM UserOrganisationMembership WHERE userID = #{userKey} AND organisationID = #{organisationID} AND (organisationRoleID = 2 OR organisationRoleID = 3)")
    boolean isUserOrganisationAdmin(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
}
