package uk.org.nbn.nbnv.api.dao.designation;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Taxon;

public interface TaxonMapper {
    final String SELECT_SPECIES_BY_CATEGORYID = "SELECT taxonVersionKey, prefnameTaxonVersionKey, taxonName, taxonAuthority, lang, taxonOutputGroupKey, taxonNavigationGroupKey from DesignationTaxonData dtd inner join NBNCore.dbo.Taxon t on dtd.pTaxonVersionKey = t.taxonVersionKey where designationID = #{id}";

    @Select(SELECT_SPECIES_BY_CATEGORYID)
    List<Taxon> selectByDesignationID(int id);
}
