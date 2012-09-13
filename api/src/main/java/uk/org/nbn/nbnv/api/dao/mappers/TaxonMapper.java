package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonWithDatasetStats;

public interface TaxonMapper {

    @Select("SELECT * FROM TaxonData WHERE taxonVersionKey = #{id}")
    Taxon getTaxon(String taxonVersionKey);
    
    @Select("SELECT taxonVersionKey, prefnameTaxonVersionKey, name, authority, lang, outputGroupKey, navigationGroupKey from DesignationTaxonData dtd inner join TaxonData t on dtd.pTaxonVersionKey = t.taxonVersionKey where designationID = #{id}")
    List<Taxon> selectByDesignationID(int id);
    
    @Select("SELECT taxonVersionKey, prefnameTaxonVersionKey, name, authority, lang, outputGroupKey, navigationGroupKey FROM DesignationTaxonData dtd INNER JOIN TaxonData t ON dtd.pTaxonVersionKey = t.taxonVersionKey WHERE designationID = #{designationId} AND t.navigationGroupKey = #{taxonNavigationGroupId} order by name")
    List<Taxon> selectByDesignationAndTaxonNavigationGroup(@Param("designationId") int designationId, @Param("taxonNavigationGroupId") String taxonNavigationGroupId);
    
    @Select("SELECT taxonVersionKey, tdt.prefnameTaxonVersionKey, name, authority, lang, outputGroupKey, navigationGroupKey, taxonGroupName outputGroupName, datasetKey, observationCount FROM TaxonDatasetTaxonData tdt INNER JOIN TaxonData t ON tdt.prefnameTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonOutputGroupData togd ON t.outputGroupKey = togd.taxonGroupKey WHERE datasetKey = #{datasetKey} ORDER BY name")
    List<TaxonWithDatasetStats> selectByDatasetKey(String datasetKey);
}
