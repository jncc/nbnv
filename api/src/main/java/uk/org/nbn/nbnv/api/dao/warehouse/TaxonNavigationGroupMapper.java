package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;

public interface TaxonNavigationGroupMapper {   
    @Select("SELECT * FROM TaxonNavigationGroupData WHERE parentTaxonGroupKey is NULL")
    @Results({ 
        @Result(column = "key", property = "children", javaType=List.class, many=@Many(select="getChildren")),
        @Result(column = "key", property = "key") //map but retain key
    })
    List<TaxonNavigationGroup> selectAll();
    
    @Select("SELECT * FROM TaxonNavigationGroupData WHERE \"key\" = #{id}")
    @Results({ 
        @Result(column = "key", property = "children", javaType=List.class, many=@Many(select="getChildren")),
        @Result(column = "key", property = "key") //map but retain key
    })
    TaxonNavigationGroup getTaxonNavigationGroup(String id);
    
    @Select("SELECT * FROM TaxonNavigationGroupData WHERE parentTaxonGroupKey = #{id}")
    List<TaxonNavigationGroup> getChildren(String id);

    @Select("SELECT * FROM TaxonNavigationGroupData WHERE parentTaxonGroupKey is NULL")
    List<TaxonNavigationGroup> getTopLevels();
    
    @Select("SELECT * FROM TaxonData WHERE taxonOutputGroupKey = #{taxonGroupKey}")
    List<Taxon> getTaxa(@Param("taxonGroupKey") String taxonGroupKey, RowBounds bounds);
    
    @Select("SELECT DISTINCT dtngd.* FROM DesignationTaxonNavigationGroupData dtngd INNER JOIN DesignationData dd ON dtngd.designationID = dd.id WHERE code = #{designationId} AND parentTaxonGroupKey IS NULL ORDER BY sortOrder ASC")
    List<TaxonNavigationGroup> getTopLevelsByDesignationID(String id);
    
    @Select("SELECT DISTINCT dtngd.* FROM DesignationTaxonNavigationGroupData dtngd INNER JOIN DesignationData dd ON dtngd.designationID = dd.id WHERE code = #{designationId} AND parentTaxonGroupKey = #{taxonNavigationGroupId} ORDER BY sortOrder ASC")
    List<TaxonNavigationGroup> getChildrenByDesignation(@Param("taxonNavigationGroupId") String taxonNavigationGroupId, @Param("designationId") String designationId);
}
