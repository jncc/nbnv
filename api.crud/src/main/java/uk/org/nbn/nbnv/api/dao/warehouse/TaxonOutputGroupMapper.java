package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

public interface TaxonOutputGroupMapper {
    
    @Select("SELECT * FROM TaxonOutputGroupData ORDER BY name")
    List<TaxonOutputGroup> selectAll();
    
    @Select("SELECT * FROM TaxonOutputGroupData WHERE \"key\" = #{id}")
    TaxonOutputGroup getById(String Id);
    
}
