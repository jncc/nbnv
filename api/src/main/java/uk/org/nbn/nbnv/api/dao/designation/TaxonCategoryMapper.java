package uk.org.nbn.nbnv.api.dao.designation;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonCategory;

public interface TaxonCategoryMapper {
    final String SELECT_TOP_LEVEL_TAXON_NAVIGATION_CATEGORIES_BY_DESIGNATIONID = "SELECT taxonGroupId, sortOrder, name, description, parent FROM  (SELECT distinct taxonGroupKey taxonGroupId, sortOrder, taxonGroupName name, descriptor description, 0 parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t  ON dtd.pTaxonVersionKey = t.taxonVersionKey  INNER JOIN TaxonGroupData tgd ON t.taxonNavigationGroupKey = tgd.taxonGroupKey WHERE parent is null AND designationID = #{id} UNION ALL SELECT distinct tgd2.taxonGroupKey taxonGroupId, tgd2.sortOrder, tgd2.taxonGroupName name, tgd2.descriptor description, 1 parent FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t  ON dtd.pTaxonVersionKey = t.taxonVersionKey  INNER JOIN TaxonGroupData tgd1 ON t.taxonNavigationGroupKey = tgd1.taxonGroupKey INNER JOIN TaxonGroupData tgd2 ON tgd1.parent = tgd2.taxonGroupKey WHERE tgd1.parent IS NOT NULL AND designationID = #{id}) a ORDER BY sortOrder ASC";
    final String SELECT_CHILD_TAXON_NAVIGATION_CATEGORIES = "SELECT distinct tgd.taxonGroupKey taxonGroupId, tgd.sortOrder, tgd.taxonGroupName name, descriptor description, 0 parent  FROM DesignationTaxonData dtd  INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey   INNER JOIN TaxonGroupData tgd ON t.taxonNavigationGroupKey = tgd.taxonGroupKey  WHERE parent = #{parentCategoryId}  AND designationID = #{designationId} ORDER BY sortOrder ASC";

    @Select(SELECT_TOP_LEVEL_TAXON_NAVIGATION_CATEGORIES_BY_DESIGNATIONID)
    List<TaxonCategory> selectTopLevelTaxonNavigationCategoriesByDesignationID(int id);

    @Select(SELECT_CHILD_TAXON_NAVIGATION_CATEGORIES)
    List<TaxonCategory> selectChildTaxonNavigationCategoriesByDesignationID(@Param("designationId") int designationId, @Param("parentCategoryId") String parentCategoryId);
}
