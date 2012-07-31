/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.taxonGroup;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    TaxonGroup getTaxonGroup(String id);
    
    @Select("SELECT * FROM TaxonGroupData WHERE parent = #{id}")
    List<TaxonGroup> getChildren(String id);

    @Select("SELECT * FROM TaxonGroupData WHERE parent is NULL")
    List<TaxonGroup> getTopLevels();
    
    @Select("SELECT taxonVersionKey, taxonName, taxonAuthority " +
        "FROM ( "+
            "SELECT taxonVersionKey, ROW_NUMBER() OVER (ORDER BY taxonname) AS RowNum "+
            "FROM NBNCore.dbo.Taxon " +
            "WHERE taxonOutputGroupKey = #{taxonGroupKey} " +
        ") AS MyDerivedTable "+
        "WHERE MyDerivedTable.RowNum BETWEEN #{start} AND #{end}"
    )
    List<Taxon> getTaxa(
        @Param("taxonGroupKey") String taxonGroupKey,
        @Param("start") int start, 
        @Param("end") int end
    );
}
