/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
public interface TaxonGroupMapper {   
    @Select("SELECT * FROM TaxonGroupData")
    List<TaxonGroup> selectAll();
    
    @Select("SELECT * FROM TaxonGroupData WHERE taxonGroupKey = #{id}")
    @Results(
        @Result(column = "taxonGroupKey", property = "children", javaType=List.class, many=@Many(select="uk.org.nbn.nbnv.api.dao.mappers.TaxonGroupMapper.getChildren"))
    )
    TaxonGroup getTaxonGroup(String id);
    
    @Select("SELECT * FROM TaxonGroupData WHERE parent = #{id}")
    List<TaxonGroup> getChildren(String id);

    @Select("SELECT * FROM TaxonGroupData WHERE parent is NULL")
    List<TaxonGroup> getTopLevels();
    
    @Select("SELECT * FROM NBNCore.dbo.Taxon WHERE taxonOutputGroupKey = #{taxonGroupKey}")
    List<Taxon> getTaxa(@Param("taxonGroupKey") String taxonGroupKey, RowBounds bounds);
    
    @Select("SELECT * from DesignationTaxonGroupData where designationID = #{id} order by sortOrder ASC")
    List<TaxonGroup> getTopLevelssByDesignationID(int id);
    
    @Select("SELECT distinct tgd.taxonGroupKey, tgd.taxonGroupName, descriptor, tgd.sortOrder, tgd.parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonGroupData tgd ON t.taxonNavigationGroupKey = tgd.taxonGroupKey WHERE parent = #{taxonGroupId} AND designationID = #{designationId} ORDER BY sortOrder ASC")
    List<TaxonGroup> getChildrenByDesignation(@Param("taxonGroupId") String taxonGroupId, @Param("designationId") int designationId);
}
