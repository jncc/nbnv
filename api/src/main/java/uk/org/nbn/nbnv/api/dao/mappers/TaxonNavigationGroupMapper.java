package uk.org.nbn.nbnv.api.dao.mappers;

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
    @Select("SELECT * FROM TaxonGroupData WHERE parent is NULL")
    @Results({ 
        @Result(column = "taxonGroupKey", property = "children", javaType=List.class, many=@Many(select="uk.org.nbn.nbnv.api.dao.mappers.TaxonNavigationGroupMapper.getChildren")),
        @Result(column = "taxonGroupKey", property = "taxonGroupKey") //map but retain key
    })
    List<TaxonNavigationGroup> selectAll();
    
    @Select("SELECT * FROM TaxonGroupData WHERE taxonGroupKey = #{id}")
    @Results({ 
        @Result(column = "taxonGroupKey", property = "children", javaType=List.class, many=@Many(select="uk.org.nbn.nbnv.api.dao.mappers.TaxonNavigationGroupMapper.getChildren")),
        @Result(column = "taxonGroupKey", property = "taxonGroupKey") //map but retain key
    })
    TaxonNavigationGroup getTaxonNavigationGroup(String id);
    
    @Select("SELECT * FROM TaxonGroupData WHERE parent = #{id}")
    List<TaxonNavigationGroup> getChildren(String id);

    @Select("SELECT * FROM TaxonGroupData WHERE parent is NULL")
    List<TaxonNavigationGroup> getTopLevels();
    
    @Select("SELECT * FROM TaxonData WHERE outputGroupKey = #{taxonGroupKey}")
    List<Taxon> getTaxa(@Param("taxonGroupKey") String taxonGroupKey, RowBounds bounds);
    
    @Select("SELECT * from DesignationTaxonNavigationGroupData WHERE designationID = #{id} AND parent IS NULL ORDER BY sortOrder ASC")
    List<TaxonNavigationGroup> getTopLevelssByDesignationID(int id);
    
    @Select("SELECT * from DesignationTaxonNavigationGroupData WHERE designationID = #{designationId} AND parent = #{taxonNavigationGroupId} ORDER BY sortOrder ASC")
    List<TaxonNavigationGroup> getChildrenByDesignation(@Param("taxonNavigationGroupId") String taxonNavigationGroupId, @Param("designationId") int designationId);
}
