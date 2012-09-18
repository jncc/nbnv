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
import uk.org.nbn.nbnv.api.dao.mappers.UserMapper;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class UserSystemAdministratorTest {
    @Autowired UserMapper userMapper;
    
    @Test
    public void notAdminTest() {        
        // Given
        User u = userMapper.getUser(43);
        
        // When
        boolean result = userMapper.isUserSystemAdministrator(u.getId());
        
        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void isAdminTest() {        
        // Given
        User u = userMapper.getUser(42);
        
        // When
        boolean result = userMapper.isUserSystemAdministrator(u.getId());
        
        // Then
        Assert.assertTrue(result);
    }
}
