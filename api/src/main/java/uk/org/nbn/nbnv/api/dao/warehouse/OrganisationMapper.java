/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public interface OrganisationMapper {
    final String SELECT_ALL = "SELECT * FROM OrganisationData";
    final String SELECT_BY_ID = "SELECT * FROM OrganisationData WHERE id = #{id}";
    final String SELECT_LOGO = "SELECT logo FROM OrganisationData WHERE id = #{id}";
    final String SELECT_LOGO_SMALL = "SELECT logoSmall FROM OrganisationData WHERE id = #{id}";
    
    @Select(SELECT_ALL)
    List<Organisation> selectAll();
    
    @Select(SELECT_BY_ID)
    Organisation selectByID(int id);
    
    @Select(SELECT_LOGO)
    Object selectLogoByOrganisationID(int id);
    
    @Select(SELECT_LOGO_SMALL)
    Object selectLogoSmallByOrganisationID(int id);
}
