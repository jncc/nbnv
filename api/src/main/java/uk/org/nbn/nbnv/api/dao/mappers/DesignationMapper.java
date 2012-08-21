package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Designation;

public interface DesignationMapper {
    final String SELECT_ALL = "SELECT * FROM DesignationData";
    final String SELECT_BY_ID = "SELECT * FROM DesignationData WHERE designationID = #{id}";
    final String SELECT_BY_CATEGORYID = "SELECT * FROM DesignationData WHERE designationCategoryID = #{id}";
    final String SELECT_BY_ID_AND_CATEGORYID = "SELECT * FROM DesignationData WHERE designationID = #{desigID} AND designationCategoryID = #{id}";
    
    @Select(SELECT_ALL)
    List<Designation> selectAll();
    
    @Select(SELECT_BY_ID)
    Designation selectByID(int id);

    @Select(SELECT_BY_CATEGORYID)
    List<Designation> selectByCategoryID(int id);
    
    @Select(SELECT_BY_ID_AND_CATEGORYID)
    Designation selectByIDAndCategoryID(@Param("desigID") int desigID, @Param("id") int id);
}
