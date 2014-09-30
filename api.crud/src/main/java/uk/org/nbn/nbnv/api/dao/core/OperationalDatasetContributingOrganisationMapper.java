/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface OperationalDatasetContributingOrganisationMapper {
    @Select("SELECT CASE WHEN logo IS NULL THEN 0 ELSE 1 END as hasLogo, CASE WHEN logoSmall IS NULL THEN 0 ELSE 1 END as hasSmallLogo, id, name, abbreviation, summary, address, postcode, contactName, contactEmail, website, allowPublicRegistration FROM OrganisationData org INNER JOIN DatasetContributingOrganisation dco ON org.id = dco.organisationID WHERE dco.datasetKey = #{key}")
    List<Organisation> selectOrganisationsByDataset(String key);
    
    @Insert("INSERT INTO DatasetContributingOrganisation VALUES (#{key}, #{id})")
    int addNewDatasetContirbutor(@Param("key") String key, @Param("id") int id);
    
    @Delete("DELETE FROM DatasetContributingOrganisation WHERE datasetKey = #{key} AND organisationID = #{id}")
    int removeExistingContributer(@Param("key") String key, @Param("id") int id);
    
}
