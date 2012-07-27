/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.taxonGroup;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
public interface TaxonGroupMapper {   
    @Select("SELECT taxonGroupKey FROM TaxonGroupData")
    List<TaxonGroup> selectAll();
    
    @Select("SELECT taxonGroupKey FROM TaxonGroupData WHERE taxonGroupKey = #{id}")
    TaxonGroup getTaxonGroup(String id);
    
    @Select("SELECT taxonGroupKey FROM TaxonGroupData WHERE parent = #{id}")
    List<TaxonGroup> getChildren(String id);
}
