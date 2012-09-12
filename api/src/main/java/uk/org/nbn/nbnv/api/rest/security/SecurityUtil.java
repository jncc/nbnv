/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.mappers.UserMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
@Component
public class SecurityUtil {
    @Autowired UserMapper userMapper;
    @Autowired OrganisationMembershipMapper organisationMembershipMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    
    public boolean IsLoggedIn(User user) {
        return user.getId() > 0;
    }
    
    public boolean IsSysAdmin(User user) {
        return userMapper.getSysAdminUser(user.getId()) != null;
    }
    
    public boolean IsUserOrganisationMember(User user, Organisation organisation) {
        return organisationMembershipMapper.selectByUserAndOrganisation(user.getId(), organisation.getOrganisationID()) != null;
    }

    public boolean IsUserOrganisationAdmin(User user, Organisation organisation) {
        OrganisationMembership membership = organisationMembershipMapper.selectByUserAndOrganisation(user.getId(), organisation.getOrganisationID());
        
        if (membership == null) {
            return false;
        }
  
        return membership.getRole() == OrganisationMembership.Role.administrator || membership.getRole() == OrganisationMembership.Role.lead;
    }
    
    public boolean IsUserDatasetAdmin(User user, Dataset dataset) {
        return datasetAdministratorMapper.selectByUserAndDataset(user.getId(), dataset.getDatasetKey()) != null;
    }
}
