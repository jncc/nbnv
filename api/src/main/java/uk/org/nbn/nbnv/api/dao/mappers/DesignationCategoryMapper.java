/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.DesignationCategory;

/**
 *
 * @author Administrator
 */
public interface DesignationCategoryMapper {
    final String SELECT_ALL = "SELECT id, label, description FROM DesignationCategoryData";
    final String SELECT_BY_ID = "SELECT id, label, description FROM DesignationCategoryData WHERE id = #{id}";
    final String SELECT_FROM_DESIGNATIONID = "SELECT dc.id, dc.label, dc.description FROM DesignationCategoryData dc INNER JOIN DesignationData d ON dc.id = d.id WHERE d.code = #{id}";

    @Select(SELECT_ALL)
    List<DesignationCategory> selectAll();
    
    @Select(SELECT_BY_ID)
    DesignationCategory selectByID(int id);
    
    @Select(SELECT_FROM_DESIGNATIONID)
    DesignationCategory selectByDesignationID(String id);
}
