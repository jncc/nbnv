/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface OperationalOrganisationMapper {
    @Select("SELECT CASE WHEN logo IS NULL THEN 0 ELSE 1 END as hasLogo, CASE WHEN logoSmall IS NULL THEN 0 ELSE 1 END as hasSmallLogo, id, name, abbreviation, summary, address, postcode, contactName, contactEmail, website, allowPublicRegistration FROM OrganisationData WHERE id = #{id}")
    Organisation selectByID(int id);
    
}
