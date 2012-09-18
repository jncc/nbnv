/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.mappers.UserMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.OrganisationMembership.Role;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class UserOrganisationMemberTest {
    @Autowired UserMapper userMapper;
    @Autowired OrganisationMapper orgMapper;
    @Autowired OrganisationMembershipMapper orgMembershipMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    
    @Test
    public void isUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(41);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        OrganisationMembership userMembership = orgMembershipMapper.selectByUserAndOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertEquals("User was expected to be an admin", Role.administrator, userMembership.getRole());
    }
    
    @Test
    public void isMemberUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(40);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        OrganisationMembership userMembership = orgMembershipMapper.selectByUserAndOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertNotSame("A member was passed in but admin rights have been detected", Role.administrator, userMembership.getRole());
    }

    @Test
    public void isNonMemberUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(43);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        OrganisationMembership userMembership = orgMembershipMapper.selectByUserAndOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertNull("Didn't expect user to have a membership", userMembership);
    }
    
    @Test
    public void isUserAnOrgMemberTest() {
        // Given
        User u = userMapper.getUser(40);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        OrganisationMembership userMembership = orgMembershipMapper.selectByUserAndOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertNotNull("Expected user to be a member", userMembership);
    }

    @Test
    public void isOrgAdminUserAnOrgMemberTest() {
        // Given
        User u = userMapper.getUser(41);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        boolean result = orgMembershipMapper.isUserMemberOfOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertTrue("Expected admin to have a user membership", result);
    }

    @Test
    public void isNonOrgUserAnOrgMemberTest() {
        // Given
        User u = userMapper.getUser(43);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        boolean result = orgMembershipMapper.isUserMemberOfOrganisation(u.getId(), org.getOrganisationID());
        
        // Then
        Assert.assertFalse("Expected non user to not be a member", result);
    }
}
