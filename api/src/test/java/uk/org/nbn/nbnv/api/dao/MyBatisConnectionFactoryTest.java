/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao;

import java.sql.SQLException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Gilbertson
 */
public class MyBatisConnectionFactoryTest {
    
    public MyBatisConnectionFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFactory method, of class MyBatisConnectionFactory.
     */
    @Test
    public void testGetFactory() throws SQLException {
        System.out.println("getFactory");
        
        SqlSessionFactory result = MyBatisConnectionFactory.getFactory();
        assertNotNull(result);
        
        SqlSession session = result.openSession();
        assertNotNull(session);
        
        try {
            assertFalse(session.getConnection().isClosed());
            assertTrue(session.getConnection().isValid(10));
        } finally {
            session.close();
        }
    }
}