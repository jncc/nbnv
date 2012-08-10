/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.persistence.EntityManagerFactory;
import uk.org.nbn.nbnv.PersistenceUtility;

/**
 *
 * @author Paul Gilbertson
 */
public class DatabaseConnection {
    public static EntityManagerFactory getInstance() {
        Map<String, String> settings = new HashMap<String, String>();
        
        ResourceBundle bundle = ResourceBundle.getBundle("settings");
        settings.put("javax.persistence.jdbc.url", bundle.getString("javax.persistence.jdbc.url"));
        settings.put("javax.persistence.jdbc.user", bundle.getString("javax.persistence.jdbc.user"));
        settings.put("javax.persistence.jdbc.password", bundle.getString("javax.persistence.jdbc.password"));
        
        return new PersistenceUtility().createEntityManagerFactory(settings);
    }
}
