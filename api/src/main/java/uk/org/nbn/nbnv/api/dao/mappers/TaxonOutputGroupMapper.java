package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonOutputGroup;

public interface TaxonOutputGroupMapper {
    
    @Select("SELECT * FROM TaxonOutputGroupData WHERE taxonGroupKey = #{id}")
    TaxonOutputGroup getById(String Id);
    
}
