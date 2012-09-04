/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Assert;
import uk.org.nbn.nbnv.api.authentication.SecurityUtil;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.mappers.UserMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class SecurityUtilTest {
    @Autowired SecurityUtil security;
    @Autowired UserMapper userMapper;
    @Autowired OrganisationMapper orgMapper;
    
    @Test
    public void notLoggedInTest() {        
        // Given
        User u = User.PUBLIC_USER;
        
        // When
        boolean result = security.IsLoggedIn(u);
        
        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void loggedInTest() {        
        // Given
        User u = userMapper.getUser(43);
        
        // When
        boolean result = security.IsLoggedIn(u);
        
        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void notAdminTest() {        
        // Given
        User u = userMapper.getUser(43);
        
        // When
        boolean result = security.IsSysAdmin(u);
        
        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void isAdminTest() {        
        // Given
        User u = userMapper.getUser(42);
        
        // When
        boolean result = security.IsSysAdmin(u);
        
        // Then
        Assert.assertTrue(result);
    }
    
    @Test
    public void isUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(41);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        boolean result = security.IsUserOrganisationAdmin(u, org);
        
        // Then
        Assert.assertTrue(result);
    }
    
    @Test
    public void isMemberUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(40);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        boolean result = security.IsUserOrganisationAdmin(u, org);
        
        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void isNonMemberUserAnOrgAdminTest() {
        // Given
        User u = userMapper.getUser(43);
        Organisation org = orgMapper.selectByID(1);
        
        //When
        boolean result = security.IsUserOrganisationAdmin(u, org);
        
        // Then
        Assert.assertFalse(result);
    }
}
