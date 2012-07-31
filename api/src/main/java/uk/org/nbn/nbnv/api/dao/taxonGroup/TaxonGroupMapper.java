/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.taxonGroup;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonGroup;

/**
 *
 * @author Administrator
 */
public interface TaxonGroupMapper {   
    @Select("SELECT * FROM TaxonGroupData")
    List<TaxonGroup> selectAll();
    
    @Select("SELECT * FROM TaxonGroupData WHERE taxonGroupKey = #{id}")
    TaxonGroup getTaxonGroup(String id);
    
    @Select("SELECT * FROM TaxonGroupData WHERE parent = #{id}")
    List<TaxonGroup> getChildren(String id);
    
    @Select("SELECT taxonGroupKey, taxonGroupName, descriptor, sortOrder, parent FROM (SELECT distinct taxonGroupKey, sortOrder, taxonGroupName, descriptor, parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonGroupData tgd ON t.taxonNavigationGroupKey = tgd.taxonGroupKey WHERE parent is null AND designationID = #{id} UNION ALL SELECT distinct tgd2.taxonGroupKey, tgd2.sortOrder, tgd2.taxonGroupName, tgd2.descriptor, tgd2.parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonGroupData tgd1 ON t.taxonNavigationGroupKey = tgd1.taxonGroupKey INNER JOIN TaxonGroupData tgd2 ON tgd1.parent = tgd2.taxonGroupKey WHERE tgd1.parent IS NOT NULL AND designationID = #{id}) a ORDER BY sortOrder ASC")
    List<TaxonGroup> selectTopLevelTaxonNavigationGroupsByDesignationID(int id);
    
    @Select("SELECT distinct tgd.taxonGroupKey, tgd.taxonGroupName, descriptor, tgd.sortOrder, tgd.parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonGroupData tgd ON t.taxonNavigationGroupKey = tgd.taxonGroupKey WHERE parent = #{taxonGroupId} AND designationID = #{designationId} ORDER BY sortOrder ASC")
    List<TaxonGroup> getChildrenByDesignation(@Param("taxonGroupId") String taxonGroupId, @Param("designationId") int designationId);

}
