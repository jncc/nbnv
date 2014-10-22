/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import uk.gov.nbn.data.properties.PropertiesReader;
import uk.org.nbn.nbnv.PersistenceUtility;

/**
 *
 * @author Paul Gilbertson
 */
public class DatabaseConnection {

    private static EntityManagerFactory factory;

    public static EntityManagerFactory getInstance() {
        try {
            Properties properties = PropertiesReader.getEffectiveProperties("settings.properties");

            if (factory == null) {
                Map<String, String> settings = new HashMap<String, String>();

                settings.put("javax.persistence.jdbc.url", properties.getProperty("javax.persistence.jdbc.url"));
                settings.put("javax.persistence.jdbc.user", properties.getProperty("javax.persistence.jdbc.user"));
                settings.put("javax.persistence.jdbc.password", properties.getProperty("javax.persistence.jdbc.password"));

                factory = new PersistenceUtility().createEntityManagerFactory(settings);
            }
            return factory;
        } catch (IOException ex) {
            return null;
        }
    }
}
