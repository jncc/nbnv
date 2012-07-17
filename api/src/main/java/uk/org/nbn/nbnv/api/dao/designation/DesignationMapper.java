/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.designation;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Designation;

/**
 *
 * @author Administrator
 */
public interface DesignationMapper {

    final String SELECT_ALL = "SELECT * FROM Designation";
    final String SELECT_BY_ID = "SELECT * FROM Designation WHERE designationID = #{id}";
    
    @Select(SELECT_ALL)
    List<Designation> selectAll();
    
    @Select(SELECT_BY_ID)
    Designation selectByID(int id);
}
