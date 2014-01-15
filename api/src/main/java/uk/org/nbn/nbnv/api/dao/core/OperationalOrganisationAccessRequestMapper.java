/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.apache.ibatis.annotations.*;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalOrganisationAccessRequestMapper {
    @Insert("INSERT INTO OrganisationAccessRequest (filterID, organisationID, datasetKey, requestPurposeID, requestReason, requestDate, sensitiveRequest)"
            + " VALUES (#{filterID}, #{organisationID}, #{datasetKey}, #{requestPurposeID}, #{requestReason}, #{requestDate}, #{sensitive})")
    public int createRequest(
            @Param("filterID") int filterID
            , @Param("organisationID") int organisationID
            , @Param("datasetKey") String datasetKey
            , @Param("requestPurposeID") int requestPurposeID
            , @Param("requestReason") String requestReason
            , @Param("requestDate") Timestamp requestDate
            , @Param("sensitive") boolean sensitiveRequest);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "WHERE uar.organisationID = #{id} AND sensitiveRequests = 0")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getOrganisationRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "WHERE uar.organisationID = #{id} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getGrantedOrganisationRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "WHERE uar.organisationID = #{id} AND responseTypeID IS NULL AND sensitiveRequest = 0")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getPendingOrganisationRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "WHERE uar.organisationID = #{organisation} AND uar.datasetKey = #{dataset} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getGrantedOrganisationRequestsByDataset(@Param("dataset") String datasetKey, @Param("organisation") int organisationID);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN UserOrganisationMembership uom ON uom.organisationID = uar.organisationID "
            + "WHERE uom.userID = #{id} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getUserGrantedOrganisationRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN UserOrganisationMembership uom ON uom.organisationID = uar.organisationID "
            + "WHERE uom.userID = #{id} AND responseTypeID IS NULL AND organisationRoleID > 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getUserPendingOrganisationRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getAdminableRequests(int id);
    
    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID IS NULL")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getPendingAdminableRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getGrantedAdminableRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "INNER JOIN DatasetAdministrator da ON da.datasetKey = uar.datasetKey "
            + "WHERE da.userID = #{id} AND responseTypeID = 2")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getDeniedAdminableRequests(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar WHERE uar.filterID = #{id}")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public OrganisationAccessRequest getRequest(int id);

    @Select("SELECT uar.* FROM OrganisationAccessRequest uar "
            + "WHERE uar.datasetKey = #{dataset} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper.selectByID")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<OrganisationAccessRequest> getGrantedRequestsByDataset(@Param("dataset") String datasetKey);
    
    @Update("UPDATE OrganisationAccessRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int acceptRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Timestamp responseDate);

    @Update("UPDATE OrganisationAccessRequest SET responseTypeID = 1, responseReason = #{responseReason}, responseDate = #{responseDate}, accessExpires = #{expiresDate} "
            + "WHERE filterID = #{filterID}")
    public int acceptRequestWithExpires(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Timestamp responseDate
            , @Param("expiresDate") Date expiresDate);

    @Update("UPDATE OrganisationAccessRequest SET responseTypeID = 2, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int denyRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Timestamp responseDate);

    @Update("UPDATE OrganisationAccessRequest SET responseTypeID = 3, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int closeRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Timestamp responseDate);

    @Update("UPDATE OrganisationAccessRequest SET responseTypeID = 4, responseReason = #{responseReason}, responseDate = #{responseDate} "
            + "WHERE filterID = #{filterID}")
    public int revokeRequest(
            @Param("filterID") int filterID
            , @Param("responseReason") String responseReason
            , @Param("responseDate") Timestamp responseDate);
}
