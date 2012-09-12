/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public interface OrganisationMapper {
    final String SELECT_ALL = "SELECT * FROM OrganisationData";
    final String SELECT_BY_ID = "SELECT * FROM OrganisationData WHERE organisationID = #{id}";
    
    @Select(SELECT_ALL)
    List<Organisation> selectAll();
    
    @Select(SELECT_BY_ID)
    Organisation selectByID(int id);
}
