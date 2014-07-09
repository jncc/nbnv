/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Paul Gilbertson
 */
public class DatabaseConnectionTest {
    
    public DatabaseConnectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public void testGetInstance() {
        EntityManagerFactory emf = DatabaseConnection.getInstance();
        EntityManager em = emf.createEntityManager();
        em.close();
    }
}
