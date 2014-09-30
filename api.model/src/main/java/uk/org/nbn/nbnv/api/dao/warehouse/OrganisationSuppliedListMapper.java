/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.OrganisationSuppliedList;

/**
 *
 * @author Matt Debont
 */
public interface OrganisationSuppliedListMapper {
    
    @Select("SELECT * FROM OrganisationSuppliedTaxonListData")
    List<OrganisationSuppliedList> selectAll();
    
    @Select("SELECT * FROM OrganisationSuppliedTaxonListData WHERE id = #{id}")
    OrganisationSuppliedList selectByID(@Param("id") int id);
    
    @Select("SELECT * FROM OrganisationSuppliedTaxonListData WHERE code = #{code}")
    OrganisationSuppliedList selectByCode(@Param("code") String code);
}
