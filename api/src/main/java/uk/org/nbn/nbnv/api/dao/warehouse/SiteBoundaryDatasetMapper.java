package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;


public interface SiteBoundaryDatasetMapper {

    @Select("SELECT * FROM SiteBoundaryDatasetData ORDER BY title")
    List<SiteBoundaryDataset> get();
    
    @Select("SELECT * FROM SiteBoundaryDatasetData where siteBoundaryCategory = #{id} order by title asc")
    List<SiteBoundaryDataset> getBySiteBoundaryCategoryID(int id);
    
    @Select("SELECT * FROM SiteBoundaryDatasetData where datasetKey = #{id}")
    SiteBoundaryDataset getByDatasetKey(String id);
    
}
