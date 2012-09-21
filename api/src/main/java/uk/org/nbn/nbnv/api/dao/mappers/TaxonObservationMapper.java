/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.mappers.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonObservation;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroupWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonWithQueryStats;

/**
 *
 * @author Paul Gilbertson
 */
public interface TaxonObservationMapper {
    @Select("SELECT * FROM UserTaxonObservationData WHERE observationID = #{id} AND userKey = #{userKey}")
    public TaxonObservation selectById(@Param("id") int id, @Param("userKey") int userKey);

    @Select("SELECT * FROM UserTaxonObservationData WHERE datasetKey = #{id} AND userKey = #{userKey}")
    public List<TaxonObservation> selectByDataset(@Param("id") String id, @Param("userKey") int userKey);

    @Select("SELECT * FROM UserTaxonObservationData WHERE pTaxonVersionKey = #{id} AND userKey = #{userKey}")
    public List<TaxonObservation> selectByPTVK(@Param("id") String id, @Param("userKey") int userKey);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectRecords")
    public List<TaxonObservation> selectObservationRecordsByFilter(
            @Param("userKey") int userKey
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("overlaps") Integer overlaps
            , @Param("within") Integer within
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("gridRef") String gridRef);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectSpecies")
    @Results(value = {
        @Result(property="taxon", column="taxonVersionKey", javaType=Taxon.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.TaxonMapper.getTaxon")),
        @Result(property="taxonVersionKey", column="taxonVersionKey")
    })
    public List<TaxonWithQueryStats> selectObservationSpeciesByFilter(
            @Param("userKey") int userKey
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("overlaps") Integer overlaps
            , @Param("within") Integer within
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("gridRef") String gridRef);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectGroups")
    @Results(value = {
        @Result(property="taxonOutputGroup", column="outputGroupKey", javaType=TaxonOutputGroup.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.TaxonOutputGroupMapper.getById")),
        @Result(property="taxonGroupKey", column="outputGroupKey")
    })
    public List<TaxonOutputGroupWithQueryStats> selectObservationGroupsByFilter(
            @Param("userKey") int userKey
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("overlaps") Integer overlaps
            , @Param("within") Integer within
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("gridRef") String gridRef);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasets")
    @Results(value = {
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper.selectByDatasetKey")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<DatasetWithQueryStats> selectObservationDatasetsByFilter(
            @Param("userKey") int userKey
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("overlaps") Integer overlaps
            , @Param("within") Integer within
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("gridRef") String gridRef);
    
}
