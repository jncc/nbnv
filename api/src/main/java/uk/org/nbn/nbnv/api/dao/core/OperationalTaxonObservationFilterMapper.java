/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalTaxonObservationFilterMapper {
    @Insert("INSERT INTO TaxonObservationFilter (filterJSON, filterText) VALUES (#{filterJSON}, #{filterText})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    public int createFilter(TaxonObservationFilter filter);
    
    @Update("UPDATE TaxonObservationFilter SET filterJSON = #{filterJSON}, filterText = #{filterText} WHERE id = #{id}")
    public int editFilter(@Param("id") int id, @Param("filterText") String filterText, @Param("filterJSON") String filterJSON);

    @Select("SELECT * FROM TaxonObservationFilter")
    public List<TaxonObservationFilter> selectAll();
    
    @Select("SELECT * FROM TaxonObservationFilter WHERE id = #{id}")
    public TaxonObservationFilter selectById(int id);
}
