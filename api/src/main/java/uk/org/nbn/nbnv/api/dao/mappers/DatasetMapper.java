package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.mappers.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.*;

public interface DatasetMapper {
    
    @Select("SELECT * FROM DatasetData ORDER BY name")
    List<Dataset> selectAll();
    
    @Select("SELECT * FROM DatasetData WHERE typeName = 'Taxon' ORDER BY name")
    List<TaxonDataset> selectAllTaxonDatasets();
    
    @Select("SELECT * FROM DatasetData WHERE datasetKey = #{datasetKey}")
    @Results(value = {
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper.selectByID")),
        @Result(property="organisationID", column="organisationID")
    })
    Dataset selectByDatasetKey(String datasetKey);
    
    @Select("SELECT * FROM DatasetData WHERE datasetKey = #{datasetKey}")
    Dataset selectByIDProviderNotInstantiated(String datasetKey);
    
    @Select("SELECT * FROM DatasetData WHERE organisationID = #{organisaionID} ORDER BY name")
    List<Dataset> selectByOrganisationID(int organisationID);

    @Select("SELECT * FROM DatasetData dd INNER JOIN TaxonDatasetData tdd ON dd.datasetKey = tdd.datasetKey WHERE dd.datasetKey = #{datasetKey}")
    @Results(value = {
        @Result(property="speciesCount", column="datasetKey", javaType=java.lang.Integer.class, one=@One(select="selectSpeciesCountByDatasetKey"))
    })
    TaxonDataset selectTaxonDatasetByID(String datasetKey);

    @Select("SELECT year(startDate) year, COUNT_BIG(*) recordCount FROM TaxonObservationData WHERE datasetKey = #{datasetKey} AND fullVersion = 1 GROUP BY datasetKey, year(startDate) ORDER BY year")
    List<YearStats> selectRecordsPerYear(String datasetKey);
    
    @Select("SELECT * FROM DatasetDateTypeRecordCountData WHERE datasetKey = #{datasetKey}")
    List<DateTypeStats> selectRecordCountPerDateTypeByDatasetKey(String datasetKey);
    
    @Select("SELECT count(*) speciesCount FROM TaxonDatasetTaxonData WHERE datasetKey = #{datasetKey}")
    Integer selectSpeciesCountByDatasetKey(String datasetKey);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasetsProviderNotInstantiated")
    List<Dataset> selectDatasetsInDesignationViewableByUser(@Param("user") User user, @Param("designation") String designationKey);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasetsProviderNotInstantiated")
    List<Dataset> selectDatasetsForTaxonViewableByUser(@Param("user") User user, @Param("ptvk") String pTaxonVersionKey);
}
