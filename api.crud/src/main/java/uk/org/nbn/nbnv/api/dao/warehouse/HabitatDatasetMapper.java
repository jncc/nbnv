package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.HabitatDataset;


public interface HabitatDatasetMapper {

    @Select("SELECT * FROM HabitatDatasetData ORDER BY title")
    List<HabitatDataset> get();

    @Select("SELECT * FROM HabitatDatasetData where datasetKey = #{id}")
    HabitatDataset getByDatasetKey(String id);    
}
