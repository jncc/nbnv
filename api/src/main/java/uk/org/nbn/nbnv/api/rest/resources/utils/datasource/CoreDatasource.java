/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils.datasource;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 *
 * @author Matt Debont
 */
public class CoreDatasource extends TransactionAwareDataSourceProxy {
    private static Logger logger = LoggerFactory.getLogger(CoreDatasource.class);
    
    public CoreDatasource(DataSource datasource, boolean coreless) {
        super(coreless ? new PlaceholderDatasource() : datasource);
        logger.info(coreless ? "Core database is running in coreless mode" : "Core database is running in normal mode");
    }
}
