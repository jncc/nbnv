/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.sql.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationJoinRequest;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
public interface OperationalOrganisationJoinRequestMapper {

    @Select("SELECT * FROM OrganisationJoinRequest WHERE id = #{id}")
    @Results(value = {
        @Result(property = "organisation", column = "organisationID", javaType = Organisation.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property = "user", column = "userID", javaType = User.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById"))
    })
    public OrganisationJoinRequest getJoinRequestByID (
            @Param("id") int id);
    
    @Select("SELECT * FROM OrganisationJoinRequest WHERE userID = #{userID} AND organisationID = #{organisationID} AND responseTypeID IS NULL")
    @Results(value = {
        @Result(property = "organisation", column = "organisationID", javaType = Organisation.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property = "user", column = "userID", javaType = User.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById"))
    })
    public OrganisationJoinRequest getActiveJoinRequestByUserAndOrganisation(
            @Param("userID") int userId,
            @Param("organisationID") int orgId);

    @Select("SELECT COUNT(*) FROM OrganisationJoinRequest WHERE userID = #{userID} AND organisationID = #{organisationID} AND responseTypeID IS NULL")
    public boolean activeOrganisationJoinRequestByUserExists(
            @Param("userID") int userId,
            @Param("organisationID") int orgId);
    
    @Select("SELECT * FROM OrganisationJoinRequest WHERE organisationID = #{organisationID} AND responseTypeID IS NULL")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID"))           
    })
    public List<OrganisationJoinRequest> getActiveJoinRequestsByOrganisation(
            @Param("organisationID") int orgId);

    @Select("SELECT * FROM OrganisationJoinRequest WHERE userID = #{userID} AND responseTypeID IS NULL")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID"))           
    })
    public List<OrganisationJoinRequest> getActiveJoinRequestsByUser(
            @Param("userID") int userID);

    @Insert("INSERT INTO OrganisationJoinRequest (userID, organisationID, requestReason, requestDate)"
            + " VALUES (#{userID}, #{organisationID}, #{requestReason}, #{requestDate})")
    public int createJoinRequest(
            @Param("userID") int userID,
            @Param("organisationID") int orgId,
            @Param("requestReason") String requestReason,
            @Param("requestDate") Date requestDate);    
    
    @Update("UPDATE OrganisationJoinRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE id = #{id} AND responseTypeID IS NULL")
    public int acceptJoinRequest(
            @Param("id") int id,
            @Param("responseReason") String responseReason,
            @Param("responseDate") Date responseDate);

    @Update("UPDATE OrganisationJoinRequest SET responseTypeID = 2, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE id = #{id} AND responseTypeID IS NULL")
    public int denyJoinRequest(
            @Param("id") int id,
            @Param("responseReason") String responseReason,
            @Param("responseDate") Date responseDate);
    
    @Update("UPDATE OrganisationJoinRequest SET responseTypeID = 3, responseDate = #{responseDate} "
            + "WHERE id = #{id}")
    public int withdrawJoinRequest(
            @Param("id") int id,
            @Param("responseDate") Date responseDate);
}
