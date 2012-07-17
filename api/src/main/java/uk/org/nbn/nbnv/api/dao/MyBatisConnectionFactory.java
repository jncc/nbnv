/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import uk.org.nbn.nbnv.api.dao.designation.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.designation.DesignationMapper;

/**
 *
 * @author Administrator
 */
public class MyBatisConnectionFactory {
    private static SqlSessionFactory sqlSessionFactory;
    
    static {
        try {
            Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
            
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                sqlSessionFactory.getConfiguration().addMapper(DesignationMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(DesignationCategoryMapper.class);
            }
        }
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static SqlSessionFactory getFactory() {
        return sqlSessionFactory;
    }
}
