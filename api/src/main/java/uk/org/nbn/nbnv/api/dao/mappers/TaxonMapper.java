package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonWithDatasetStats;

public interface TaxonMapper {

    @Select("SELECT * FROM TaxonData WHERE taxonVersionKey = #{id}")
    Taxon getTaxon(String taxonVersionKey);
    
    @Select("SELECT taxonVersionKey, t.pTaxonVersionKey, name, authority, languageKey, taxonOutputGroupKey from DesignationTaxonData dtd inner join TaxonData t on dtd.pTaxonVersionKey = t.taxonVersionKey where code = #{id}")
    List<Taxon> selectByDesignationID(String id);
    
    @Select("SELECT t.taxonVersionKey, t.pTaxonVersionKey, name, authority, languageKey, taxonOutputGroupKey FROM DesignationTaxonData dtd INNER JOIN TaxonData t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonNavigationData tnd ON t.pTaxonVersionKey = tnd.taxonVersionKey WHERE code = #{designationId} AND tnd.taxonNavigationGroupKey = #{taxonNavigationGroupId} order by name")
    List<Taxon> selectByDesignationAndTaxonNavigationGroup(@Param("designationId") String designationId, @Param("taxonNavigationGroupId") String taxonNavigationGroupId);
    
    @Select("SELECT taxonVersionKey, tdt.prefnameTaxonVersionKey, name, authority, lang, outputGroupKey, navigationGroupKey, taxonGroupName outputGroupName, datasetKey, observationCount FROM TaxonDatasetTaxonData tdt INNER JOIN TaxonData t ON tdt.prefnameTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonOutputGroupData togd ON t.outputGroupKey = togd.taxonGroupKey WHERE datasetKey = #{datasetKey} ORDER BY name")
    List<TaxonWithDatasetStats> selectByDatasetKey(String datasetKey);
}
