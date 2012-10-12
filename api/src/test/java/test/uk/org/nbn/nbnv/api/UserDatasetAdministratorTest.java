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
    public void isUserAnDatasetAdminTest() {
        // Given
        User u = userMapper.getUser(39);
        Dataset ds = datasetMapper.selectByDatasetKey("DATASET1");
        
        //When
        boolean result = datasetAdministratorMapper.isUserDatasetAdministrator(u.getId(), ds.getDatasetKey());   
        
        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void isNotDatasetAdminUserAnDatasetAdminTest() {
        // Given
        User u = userMapper.getUser(43);
        Dataset ds = datasetMapper.selectByDatasetKey("DATASET1");
        
        // When
        boolean result = datasetAdministratorMapper.isUserDatasetAdministrator(u.getId(), ds.getDatasetKey());
        
        // Then
        Assert.assertFalse(result);
    }
}