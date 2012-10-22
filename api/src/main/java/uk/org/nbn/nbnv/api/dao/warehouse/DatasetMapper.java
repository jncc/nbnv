package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.*;

public interface DatasetMapper {
    
    @Select("SELECT * FROM DatasetData ORDER BY title")
    List<Dataset> selectAll();
    
    @Select("SELECT * FROM DatasetData WHERE typeName = 'Taxon' ORDER BY title")
    List<TaxonDataset> selectAllTaxonDatasets();
    
    @Select("SELECT * FROM DatasetData WHERE DatasetData.[key] = #{key}")
    @Results(value = {
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID")),
        @Result(property="organisationID", column="organisationID")
    })
    Dataset selectByDatasetKey(String key);
    
    @Select("SELECT * FROM DatasetData WHERE DatasetData.[key] = #{key}")
    Dataset selectByIDProviderNotInstantiated(String key);
    
    @Select("SELECT * FROM DatasetData WHERE organisationID = #{organisaionID} ORDER BY title")
    List<Dataset> selectByOrganisationID(int organisationID);

    @Select("SELECT * FROM DatasetData dd INNER JOIN TaxonDatasetData tdd ON dd.[key] = tdd.datasetKey WHERE dd.[key] = #{key}")
    @Results(value = {
        @Result(property="speciesCount", column="key", javaType=java.lang.Integer.class, one=@One(select="selectSpeciesCountByDatasetKey")),
        @Result(property="key", column="key")
    })
    TaxonDataset selectTaxonDatasetByID(String key);

    @Select("SELECT year(startDate) year, COUNT_BIG(*) recordCount FROM TaxonObservationData WHERE datasetKey = #{key} AND fullVersion = 1 AND YEAR(startDate) <> 0 GROUP BY datasetKey, year(startDate) ORDER BY year")
    List<YearStats> selectRecordsPerYear(String key);
    
    @Select("SELECT * FROM DatasetDateTypeRecordCountData WHERE datasetKey = #{key}")
    List<DateTypeStats> selectRecordCountPerDateTypeByDatasetKey(String key);
    
    @Select("SELECT count(*) speciesCount FROM TaxonDatasetTaxonData WHERE datasetKey = #{key}")
    Integer selectSpeciesCountByDatasetKey(String key);

    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasetsProviderNotInstantiated")
    List<Dataset> selectDatasetsInDesignationViewableByUser(@Param("user") User user, @Param("designation") String designationKey);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filteredSelectDatasetsProviderNotInstantiated")
    List<Dataset> selectDatasetsForTaxonViewableByUser(@Param("user") User user, @Param("ptvk") String pTaxonVersionKey);
}
