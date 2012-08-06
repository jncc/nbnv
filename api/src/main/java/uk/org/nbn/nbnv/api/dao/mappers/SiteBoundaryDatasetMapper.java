package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;


public interface SiteBoundaryDatasetMapper {

    @Select("SELECT * FROM SiteBoundaryDataset")
    List<SiteBoundaryDataset> get();
    
    @Select("SELECT * FROM SiteBoundaryDataset where siteBoundaryCategory = #{id}")
    List<SiteBoundaryDataset> getBySiteBoundaryCategoryID(int id);
    
}
