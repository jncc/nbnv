package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationDownloadProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DownloadReport;
import uk.org.nbn.nbnv.api.model.DownloadStat;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonObservation;
import uk.org.nbn.nbnv.api.model.TaxonObservationAttributeValue;
import uk.org.nbn.nbnv.api.model.TaxonObservationDownload;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroupWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.DatasetRecordCount;

/**
 *
 * @author Paul Gilbertson
 */
//@CacheNamespace(implementation=org.mybatis.caches.oscache.OSCache.class)
public interface TaxonObservationMapper {
    //@Select("SELECT * FROM UserTaxonObservationData WHERE observationID = #{id} AND userID = #{userKey}")
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecords")
	public TaxonObservation selectById(@Param("user") User user, @Param("id") int id);

//    //@Select("SELECT * FROM UserTaxonObservationData WHERE datasetKey = #{id} AND userID = #{userKey}")
//	@SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecords")
//    public List<TaxonObservation> selectByDataset(@Param("user") User user, @Param("datasetKey") List<String> datasetKey);
//
//    //@Select("SELECT * FROM UserTaxonObservationData WHERE pTaxonVersionKey = #{id} AND userID = #{userKey}")
//	@SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecords")
//    public List<TaxonObservation> selectByPTVK(@Param("user") User user, @Param("ptvk") List<String> ptvk );
    
//    //@Select("SELECT TOP 1 absence FROM UserTaxonObservationData WHERE pTaxonVersionKey = #{id} AND userID = #{userKey} AND absence = #{absence}")
//	@SelectProvider(type=TaxonObservationProvider.class, method="pTVKHasAbsence")
//    public Integer pTVKHasGridAbsence(@Param("id") String id, @Param("userKey") int userKey, @Param("absence") Boolean absence);
//    
//    //@Select("SELECT TOP 1 absence FROM UserTaxonObservationData WHERE pTaxonVersionKey = #{id} AND userID = #{userKey} AND polygonKey IS NOT NULL AND absence = #{absence}")
//	@SelectProvider(type=TaxonObservationProvider.class, method="pTVKHasAbsence")
//    public Integer pTVKHasPolygonAbsence(@Param("id") String id, @Param("userKey") int userKey, @Param("absence") Boolean absence, @Param("polygonQuery") Boolean polygonQuery);
	
	@SelectProvider(type=TaxonObservationProvider.class, method="pTVKHasAbsence")
	public Integer pTVKHasAbsence(@Param("user") User user, @Param("ptvk") String ptvk, @Param("absence") Boolean absence, @Param("polygonQuery") Boolean polygonQuery);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecords")
    public List<TaxonObservation> selectObservationRecordsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon
            , @Param("absence") Boolean absence);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableRecordIDs")
    public List<Integer> selectRequestableObservationRecordIDsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableRecordIDsOrganisation")
    public List<Integer> selectRequestableObservationRecordIDsByFilterOrganisation(
            @Param("organisation") Organisation organisation
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectOneAttribute")
    public List<TaxonObservationAttributeValue> selectObservationAttributeByFilter(
            @Param("user") User user
            , @Param("datasetKey") String datasetKey
            , @Param("attributeID") Integer attributeID
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecordsOrderedByDataset")
    public List<TaxonObservation> selectObservationsByFilterOrderedByDataset(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectSpecies")
    @Results(value = {
        @Result(property="taxon", column="pTaxonVersionKey", javaType=Taxon.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper.getTaxon")),
        @Result(property="taxonVersionKey", column="pTaxonVersionKey")
    })
    public List<TaxonWithQueryStats> selectObservationSpeciesByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon
            , @Param("absence") Boolean absence);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectGroups")
    @Results(value = {
        @Result(property="taxonOutputGroup", column="taxonOutputGroupKey", javaType=TaxonOutputGroup.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.TaxonOutputGroupMapper.getById")),
        @Result(property="taxonOutputGroupKey", column="taxonOutputGroupKey")
    })
    public List<TaxonOutputGroupWithQueryStats> selectObservationGroupsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon
            , @Param("absence") Boolean absence);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasets")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectObservationDatasetsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectAllDatasets")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectAllObservationDatasetsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableDatasets")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableDatasetsOrganisation")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilterOrganisation(
            @Param("organisation") Organisation organisation
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableSensitiveDatasets")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRequestableSensitiveDatasetsOrganisation")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=TaxonDataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilterOrganisation(
            @Param("organisation") Organisation organisation
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectUnavailableDatasets")
    @Results(value = {
        @Result(property="taxonDataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByIDProviderNotInstantiated")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<TaxonDatasetWithQueryStats> selectUnavailableDatasetsByFilter(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type= TaxonObservationProvider.class, method="filteredDownloadRecords")
    @Options(useCache = false, flushCache = true)
    public List<TaxonObservationDownload> selectDownloadableRecords(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    /**
     * Download Reporting
     */
    @SelectProvider(type = TaxonObservationDownloadProvider.class, method="selectDownloadReportsForDataset")
    public List<DownloadReport> selectDownloadReportsByDataset(
            @Param("datasetKey") List<String> datasetKey,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("filterID") List<Integer> filterID,
            @Param("userID") List<Integer> userID,
            @Param("organisationID") List<Integer> organisationID,
            @Param("purposeID") List<Integer> purposeID);
    
    @SelectProvider(type = TaxonObservationDownloadProvider.class, method="selectDownloadStats")
    public List<DownloadStat> selectDownloadStats(
            @Param("datasetKey") List<String> datasetKey,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("filterID") List<Integer> filterID,
            @Param("userID") List<Integer> userID,
            @Param("organisationID") List<Integer> organisationID,
            @Param("purposeID") List<Integer> purposeID);
    
    @SelectProvider(type = TaxonObservationDownloadProvider.class, method="selectUserDownloadStats")
    public List<DownloadStat> selectUserDownloadStats(
            @Param("datasetKey") List<String> datasetKey,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("filterID") List<Integer> filterID,
            @Param("userID") List<Integer> userID,
            @Param("organisationID") List<Integer> organisationID,
            @Param("purposeID") List<Integer> purposeID);
    
    @SelectProvider(type = TaxonObservationDownloadProvider.class, method="selectOrganisationDownloadStats")
    public List<DownloadStat> selectOrganisationDownloadStats(
            @Param("datasetKey") List<String> datasetKey,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("filterID") List<Integer> filterID,
            @Param("userID") List<Integer> userID,
            @Param("organisationID") List<Integer> organisationID,
            @Param("purposeID") List<Integer> purposeID);
    
    @SelectProvider(type = TaxonObservationProvider.class, method = "getFilteredRecordCountByDataset")
    public List<DatasetRecordCount> getRecordCountsForFilterByDataset(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon
            , @Param("absence") Boolean absence);
  
//    Might reactivate this later searches for users that have downloaded from a list of datasets    
//    @SelectProvider(type=TaxonObservationDownloadProvider.class, method="selectDistinctUsersForDatasets")
//    public List<User> selectDistinctUsersForDatasets(
//            @Param("dataset") List<String> datasetKey
//    );
}
