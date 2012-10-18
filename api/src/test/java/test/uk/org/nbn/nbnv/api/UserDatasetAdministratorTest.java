/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.nbnv.api;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.UserMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@DirtiesContext
public class UserDatasetAdministratorTest {
    @Autowired UserMapper userMapper;
    @Autowired DatasetMapper datasetMapper;
    @Autowired DatasetAdministratorMapper datasetAdministratorMapper;
    
    @Test
    @Ignore
    public void isUserAnDatasetAdminTest() {
        // Given
        User u = userMapper.getUser(39);
        Dataset ds = datasetMapper.selectByDatasetKey("DATASET1");
        
        //When
        boolean result = datasetAdministratorMapper.isUserDatasetAdministrator(u.getId(), ds.getKey());   
        
        // Then
        Assert.assertTrue(result);
    }

    @Test
    @Ignore
    public void isNotDatasetAdminUserAnDatasetAdminTest() {
        // Given
        User u = userMapper.getUser(43);
        Dataset ds = datasetMapper.selectByDatasetKey("DATASET1");
        
        // When
        boolean result = datasetAdministratorMapper.isUserDatasetAdministrator(u.getId(), ds.getKey());
        
        // Then
        Assert.assertFalse(result);
    }
}
