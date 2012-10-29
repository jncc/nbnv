package uk.gov.nbn.data.api.solr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The following class will set the solr.solr.home system property to the
 * solr-home folder contained in the WEB-INF. This allows the creation of a 
 * preconfigured solr.war
 * @author Christopher Johnson
 */
public class WebInfSolrHomeSettingContextListener implements ServletContextListener  {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("solr.solr.home", sce.getServletContext().getRealPath("WEB-INF/solr-home/"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}

}
