/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.sql.Date;
import java.util.List;
import org.apache.ibatis.annotations.*;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserAccessRequestMapper {
    @Insert("INSERT INTO UserAccessRequest (filterID, userID, datasetKey, requestRoleID, requestTypeID, requestReason, requestDate)"
            + " VALUES (#{filterID}, #{userID}, #{datasetKey}, #{requestRoleID}, #{requestTypeID}, #{requestReason}, #{requestDate})")
    public int createRequest(
            @Param("filterID") int filterID
            , @Param("userID") int userID
            , @Param("datasetKey") String datasetKey
            , @Param("requestRoleID") int requestRoleID
            , @Param("requestTypeID") int requestTypeID
            , @Param("requestReason") String requestReason
            , @Param("requestDate") Date requestDate);

    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById"))
    })
    public List<UserAccessRequest> getAdminableRequests(int id);
    
    @Select("SELECT uar.* FROM UserAccessRequest uar WHERE filterID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById"))
    })
    public UserAccessRequest getRequest(int id);
    
    @Update("UPDATE UserAccessRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int acceptRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Date responseDate);

    @Update("UPDATE UserAccessRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate}, accessExpires = #{expiresDate} "
            + "WHERE filterID = #{filterID}")
    public int acceptRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Date responseDate
            , @Param("expiresDate") Date expiresDate);

    @Update("UPDATE UserAccessRequest SET responseTypeID = 2, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int denyRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Date responseDate);

}
