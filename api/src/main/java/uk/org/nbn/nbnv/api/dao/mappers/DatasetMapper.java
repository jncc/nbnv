package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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
    
    @Select("SELECT * FROM DatasetData WHERE organisationID = #{organisaionID} ORDER BY name")
    List<Dataset> selectByOrganisationID(int organisationID);

    @Select("SELECT * FROM DatasetData dd INNER JOIN TaxonDatasetData tdd ON dd.datasetKey = tdd.datasetKey WHERE dd.datasetKey = #{datasetKey}")
    @Results(value = {
        @Result(property="speciesCount", column="datasetKey", javaType=java.lang.Integer.class, one=@One(select="selectSpeciesCountByDatasetKey"))
    })
    TaxonDataset selectTaxonDatasetByID(String datasetKey);

    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("select year(startDate) year, count(*) recordCount from TaxonObservationData where datasetKey = #{datasetKey} group by year(startDate) order by year(startDate)")
    List<YearStats> selectRecordsPerYear(String datasetKey);
    
    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("SELECT label dateTypeName, count(*) recordCount FROM TaxonObservationData tod INNER JOIN NBNCore.dbo.DateType d ON tod.dateType = d.dateTypeKey WHERE datasetKey = #{datasetKey} GROUP BY label ORDER BY label")
    List<DateTypeStats> selectRecordCountPerDateTypeByDatasetKey(String datasetKey);
    
    @Select("SELECT count(*) speciesCount FROM TaxonDatasetTaxonData WHERE datasetKey = #{datasetKey}")
    Integer selectSpeciesCountByDatasetKey(String datasetKey);

}
