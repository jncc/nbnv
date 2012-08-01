package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Taxon;

public interface TaxonMapper {

    @Select("SELECT taxonVersionKey, prefnameTaxonVersionKey, taxonName, taxonAuthority, lang, taxonOutputGroupKey, taxonNavigationGroupKey from DesignationTaxonData dtd inner join NBNCore.dbo.Taxon t on dtd.pTaxonVersionKey = t.taxonVersionKey where designationID = #{id}")
    List<Taxon> selectByDesignationID(int id);
    
    @Select("SELECT taxonVersionKey, prefnameTaxonVersionKey, taxonName, taxonAuthority, lang, taxonOutputGroupKey, taxonNavigationGroupKey FROM DesignationTaxonData dtd INNER JOIN NBNCore.dbo.Taxon t ON dtd.pTaxonVersionKey = t.taxonVersionKey WHERE designationID = #{designationId} AND t.taxonNavigationGroupKey = #{taxonGroupId}")
    List<Taxon> selectByDesignationAndTaxonNavigationGroup(@Param("designationId") int designationId, @Param("taxonGroupId") String taxonGroupId);
}
