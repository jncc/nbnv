package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonWebLink;
import uk.org.nbn.nbnv.api.model.TaxonWithDatasetStats;

public interface TaxonMapper {
    @Select("SELECT t.*, ct.name AS commonName FROM TaxonData t LEFT JOIN TaxonData ct ON ct.taxonVersionKey = t.commonNameTaxonVersionKey WHERE t.taxonVersionKey = #{id}")
    Taxon getTaxon(String taxonVersionKey);
    
    @Select("SELECT taxonVersionKey, t.pTaxonVersionKey, name, authority, languageKey, taxonOutputGroupKey from DesignationTaxonData dtd inner join TaxonData t on dtd.pTaxonVersionKey = t.taxonVersionKey where code = #{id}")
    List<Taxon> selectByDesignationID(String id);
    
    @Select("SELECT t.taxonVersionKey, t.pTaxonVersionKey, name, authority, languageKey, taxonOutputGroupKey FROM DesignationTaxonData dtd INNER JOIN TaxonData t ON dtd.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonNavigationData tnd ON t.pTaxonVersionKey = tnd.taxonVersionKey WHERE code = #{designationId} AND tnd.taxonNavigationGroupKey = #{taxonNavigationGroupId} order by name")
    List<Taxon> selectByDesignationAndTaxonNavigationGroup(@Param("designationId") String designationId, @Param("taxonNavigationGroupId") String taxonNavigationGroupId);
    
    @Select("SELECT st.*, ct.name AS commonName "
            + "FROM TaxonData t "
            + "INNER JOIN TaxonData st ON st.pTaxonVersionKey = t.pTaxonVersionKey "
            + "LEFT JOIN TaxonData ct ON ct.taxonVersionKey = st.commonNameTaxonVersionKey "
            + "WHERE t.taxonVersionKey = #{id} "
            + "AND st.taxonVersionKey != t.taxonVersionKey")
    List<Taxon> selectSynonymsByTVK(String taxonVersionKey);
    
    @Select("SELECT taxonVersionKey, tdt.pTaxonVersionKey, t.name, authority, languageKey, taxonOutputGroupKey, datasetKey, observationCount, togd.name taxonOutputGroupName FROM TaxonDatasetTaxonData tdt INNER JOIN TaxonData t ON tdt.pTaxonVersionKey = t.taxonVersionKey INNER JOIN TaxonOutputGroupData togd ON t.taxonOutputGroupKey = togd.\"key\" WHERE datasetKey = #{datasetKey} ORDER BY name")
    List<TaxonWithDatasetStats> selectByDatasetKey(String datasetKey);

    @Select("SELECT tp.*, ct.name AS commonName "
            + "FROM TaxonData t "
            + "INNER JOIN OrganismData o ON o.[key] = t.organismKey "
            + "INNER JOIN TaxonData tp ON tp.organismKey = o.parentOrganismKey AND tp.taxonVersionKey = tp.pTaxonVersionKey "
            + "LEFT JOIN TaxonData ct ON ct.taxonVersionKey = tp.commonNameTaxonVersionKey "
            + "WHERE t.taxonVersionKey = #{id}")
    Taxon getParentTaxon(String taxonVersionKey);

    @Select("WITH tree (organismKey, parentOrganismKey, [level]) AS ("
            + "	SELECT o.[key], o.parentOrganismKey, 0"
            + "	FROM OrganismData o"
            + "	INNER JOIN TaxonData td ON td.organismKey = o.[key] "
            + "	WHERE td.taxonVersionKey = #{id}"
            + "	UNION ALL"
            + "	SELECT o.[key], o.parentOrganismKey, t.[level] + 1"
            + "	FROM OrganismData o"
            + "	INNER JOIN tree t ON t.parentOrganismKey = o.[key] )"
            + " SELECT td.* FROM tree t"
            + " INNER JOIN TaxonData td ON td.organismKey = t.organismKey AND td.pTaxonVersionKey = td.taxonVersionKey "
            + " WHERE t.[level] > 0"
            + "ORDER BY t.[level] DESC ")
    List<Taxon> selectAncestryByTVK(String taxonVersionKey);
    
    @Select("SELECT tp.*, ct.name AS commonName "
            + "FROM TaxonData t "
            + "INNER JOIN OrganismData o ON o.parentOrganismKey = t.organismKey "
            + "INNER JOIN TaxonData tp ON tp.organismKey = o.[key] AND tp.taxonVersionKey = tp.pTaxonVersionKey "
            + "LEFT JOIN TaxonData ct ON ct.taxonVersionKey = tp.commonNameTaxonVersionKey "
            + "WHERE t.taxonVersionKey = #{id}")
    List<Taxon> selectChildrenByTVK(String taxonVersionKey);

    @Select("SELECT * FROM TaxonWebLink twl WHERE twl.taxonVersionKey = #{id} AND twl.active = 1")
    @Results(value = {
        @Result(property="taxon", column="taxonVersionKey", javaType=Taxon.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper.getTaxon"))
    })
    List<TaxonWebLink> getActiveWebLinksByTVK(String taxonVersionKey);
}
