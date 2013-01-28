/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface DatasetContributingOrganisationMapper {
    @Select(OrganisationMapper.SELECT_ALL + " org INNER JOIN DatasetContributingOrganisation dco ON org.id = dco.organisationID WHERE dco.datasetKey = #{key}")
    List<Organisation> selectOrganisationsByDataset(String key);
    
}
