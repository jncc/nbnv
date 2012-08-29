package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.TaxonDataset;

public interface DatasetMapper {
    
    @Select("SELECT * FROM DatasetData ORDER BY name")
    List<Dataset> selectAll();
    
    @Select("SELECT * FROM DatasetData WHERE typeName = 'Taxon' ORDER BY name")
    List<TaxonDataset> selectAllTaxonDatasets();
    
    @Select("SELECT * FROM DatasetData WHERE datasetKey = #{datasetKey}")
    Dataset selectByDatasetKey(String datasetKey);
    
    @Select("SELECT * FROM DatasetData WHERE datasetKey = #{datasetKey}")
    TaxonDataset selectTaxonDatasetByID(String datasetKey);
    
}
