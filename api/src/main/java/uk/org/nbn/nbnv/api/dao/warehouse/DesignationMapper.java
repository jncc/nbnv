package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Designation;
import uk.org.nbn.nbnv.api.model.TaxonDesignation;

public interface DesignationMapper {
    final String SELECT_ALL = "SELECT * FROM DesignationData";
    final String SELECT_BY_ID = "SELECT * FROM DesignationData WHERE code = #{id}";
    final String SELECT_BY_CATEGORYID = "SELECT * FROM DesignationData WHERE designationCategoryID = #{id}";
    final String SELECT_BY_ID_AND_CATEGORYID = "SELECT * FROM DesignationData WHERE code = #{desigID} AND designationCategoryID = #{id}";
    
    @Select(SELECT_ALL)
    List<Designation> selectAll();
    
    @Select(SELECT_BY_ID)
    Designation selectByID(String id);

    @Select(SELECT_BY_CATEGORYID)
    List<Designation> selectByCategoryID(int id);
    
    @Select(SELECT_BY_ID_AND_CATEGORYID)
    Designation selectByIDAndCategoryID(@Param("desigID") String desigID, @Param("id") int id);
    
    @Select("SELECT dtd.* FROM DesignationData d "
            + "INNER JOIN DesignationTaxonData dtd ON dtd.designationID = d.id "
            + "INNER JOIN TaxonData t ON t.pTaxonVersionKey = dtd.pTaxonVersionKey "
            + "WHERE t.taxonVersionKey = #{id}")
    @Results(value = {
        @Result(property="designation", column="code", javaType=Designation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DesignationMapper.selectByID"))
    })
    List<TaxonDesignation> selectByTaxonVersionKey(String taxonVersionKey);
}
