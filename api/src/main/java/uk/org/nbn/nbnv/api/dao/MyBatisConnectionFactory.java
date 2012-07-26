package uk.org.nbn.nbnv.api.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import uk.org.nbn.nbnv.api.dao.designation.DesignationCategoryMapper;
import uk.org.nbn.nbnv.api.dao.designation.DesignationMapper;
import uk.org.nbn.nbnv.api.dao.designation.TaxonCategoryMapper;
import uk.org.nbn.nbnv.api.dao.designation.TaxonMapper;

/**
 *
 * @author Administrator
 */
public class MyBatisConnectionFactory {
    private static SqlSessionFactory sqlSessionFactory = null;
    private static Reader reader = null;
    
    static {
        try {
            reader = Resources.getResourceAsReader("SqlMapConfig.xml");
        } catch (IOException ex) {
            Logger.getLogger(MyBatisConnectionFactory.class.getName()).log(Level.SEVERE, "Cannot read MyBatis config file: SqlMapConfig.xml", ex);
        }
    }

    public static SqlSessionFactory getFactory() {
        if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                sqlSessionFactory.getConfiguration().addMapper(DesignationMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(DesignationCategoryMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(TaxonMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(TaxonCategoryMapper.class);
        }
        return sqlSessionFactory;
    }
}