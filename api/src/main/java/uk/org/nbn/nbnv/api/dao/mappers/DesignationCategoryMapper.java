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
    final String SELECT_ALL = "SELECT designationCategoryID, label, description FROM DesignationCategoryData";
    final String SELECT_BY_ID = "SELECT designationCategoryID, label, description FROM DesignationCategoryData WHERE designationCategoryID = #{id}";
    final String SELECT_FROM_DESIGNATIONID = "SELECT dc.designationCategoryID, dc.label, dc.description FROM DesignationCategory dc INNER JOIN DesignationData d ON dc.designationCategoryID = d.designationCategoryID WHERE d.designationID = #{id}";

    @Select(SELECT_ALL)
    List<DesignationCategory> selectAll();
    
    @Select(SELECT_BY_ID)
    DesignationCategory selectByID(int id);
    
    @Select(SELECT_FROM_DESIGNATIONID)
    DesignationCategory selectByDesignationID(int id);
}
