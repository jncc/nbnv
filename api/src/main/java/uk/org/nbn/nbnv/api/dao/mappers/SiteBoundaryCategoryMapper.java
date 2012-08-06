package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import uk.org.nbn.nbnv.api.model.SiteBoundaryCategory;

public interface SiteBoundaryCategoryMapper {
    
    @Select("SELECT * FROM SiteBoundaryCategory")
    List<SiteBoundaryCategory> get();
    
    @Select("SELECT * FROM SiteBoundaryCategory where siteBoundaryCategoryID = #{id}")
    SiteBoundaryCategory getByID(int id);

}
