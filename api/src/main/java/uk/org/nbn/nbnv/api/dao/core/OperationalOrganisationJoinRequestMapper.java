/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.sql.Date;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author Matt Debont
 */
public interface OperationalOrganisationJoinRequestMapper {

    @Insert("INSERT INTO OrganisationJoinRequest (userID, organisationID, requestReason, requestDate)"
            + " VALUES (#{userID}, #{organistationID}, #{requestReason}, #{requestDate})")
    public int createJoinRequest(
            @Param("userID") int userID,
            @Param("organisationID") int orgId,
            @Param("requestReason") String requestReason,
            @Param("requestDate") Date requestDate);

    @Select("SELECT userID, requestReason, requestDate, responseID, responseReason, responseDate FROM OrganisationJoinRequest WHERE userID = #{userID} AND organisationID = #{organistaionID}")
    public int getJoinRequestByUserAndOrganisation(
            @Param("userID") int userId,
            @Param("organisationID") int orgId);

    @Update("UPDATE OrganisationJoinRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE userID = #{userID}")
    public int acceptJoinRequest(
            @Param("userID") int userId,
            @Param("responseReason") String responseReason,
            @Param("responseDate") Date responseDate,
            @Param("expiresDate") Date expiresDate);

    @Update("UPDATE OrganisationJoinRequest SET responseTypeID = 2, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE userID = #{userID}")
    public int denyJoinRequest(
            @Param("userID") int userId,
            @Param("responseReason") String responseReason,
            @Param("responseDate") Date responseDate,
            @Param("expiresDate") Date expiresDate);
}
