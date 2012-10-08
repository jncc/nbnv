package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

public interface TaxonOutputGroupMapper {
    
    @Select("SELECT * FROM TaxonOutputGroupData")
    List<TaxonOutputGroup> selectAll();
    
    @Select("SELECT * FROM TaxonOutputGroupData WHERE taxonGroupKey = #{id}")
    TaxonOutputGroup getById(String Id);
    
}
