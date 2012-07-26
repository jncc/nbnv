/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class PropertiesReader {
    private static final File PROPERTIES_OVERLOAD_DIR = new File("/server/overload/");
    
    public static Properties getEffectiveProperties(String name) throws IOException {
        Properties localProperties = readStreamAndClose(PowerlessServlet.class.getClassLoader().getResourceAsStream(name));
        
        File overloadingProperitesFile = new File(PROPERTIES_OVERLOAD_DIR, name);
        if(overloadingProperitesFile.isFile()) {
            localProperties.putAll(readStreamAndClose(new FileInputStream(overloadingProperitesFile)));
        }
        return localProperties;
    }
    
    private static Properties readStreamAndClose(InputStream toReadFrom) throws IOException {
        try {
            Properties toReturn = new Properties();
            toReturn.load(toReadFrom);
            return toReturn;
        }
        finally {
            toReadFrom.close();
        }
    } 
}
