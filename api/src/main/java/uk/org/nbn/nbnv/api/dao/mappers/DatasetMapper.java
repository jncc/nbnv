package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Dataset;

public interface DatasetMapper {
    
    @Select("SELECT * FROM DatasetData")
    List<Dataset> selectAll();
    
}
