/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.sql.Date;
import java.util.List;
import org.apache.ibatis.annotations.*;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserAccessRequestMapper {
    @Insert("INSERT INTO UserAccessRequest (filterID, userID, datasetKey, requestPurposeID, requestReason, requestDate)"
            + " VALUES (#{filterID}, #{userID}, #{datasetKey}, #{requestPurposeID}, #{requestReason}, #{requestDate})")
    public int createRequest(
            @Param("filterID") int filterID
            , @Param("userID") int userID
            , @Param("datasetKey") String datasetKey
            , @Param("requestPurposeID") int requestPurposeID
            , @Param("requestReason") String requestReason
            , @Param("requestDate") Date requestDate);

    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey"))
    })
    public List<UserAccessRequest> getAdminableRequests(int id);
    
    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID IS NULL")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey"))
    })
    public List<UserAccessRequest> getPendingAdminableRequests(int id);

    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey"))
    })
    public List<UserAccessRequest> getGrantedAdminableRequests(int id);

    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID = 2")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey"))
    })
    public List<UserAccessRequest> getDeniedAdminableRequests(int id);

    @Select("SELECT uar.* FROM UserAccessRequest uar WHERE uar.filterID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById"))
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
    public int acceptRequestWithExpires(
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
