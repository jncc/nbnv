/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.util.properties;

import java.util.ResourceBundle;

/**
 *
 * @author Administrator
 */
public class ServerSpecificProperties {
    private static final String _propertiesFile = "nbnServerSpecificProperties";

    public static String getGISServerName() {
        return retrieveProperty("nbn.gis.server");
    }

    public static String getSLDServerName() {
        return retrieveProperty("nbn.gis.sld.server");
    }

    public static int getSLDServerPort() {
        return Integer.parseInt(retrieveProperty("nbn.gis.sld.port"));
    }

    private static String retrieveProperty(String propertyName) {
        return ResourceBundle.getBundle(_propertiesFile).getString(propertyName);
    }
}
