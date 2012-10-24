package uk.gov.nbn.data.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The following reader will enable us to load properties files which can have
 * server specific properties loaded in preference to those defined in the 
 * application
 * @author Christopher Johnson
 */
public class PropertiesReader {
    private static final File PROPERTIES_OVERLOAD_DIR = new File("/nbnv-settings/");
    
    public static Properties getEffectiveProperties(String name) throws IOException {
        Properties localProperties = readStreamAndClose(PropertiesReader.class.getClassLoader().getResourceAsStream(name));
        
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
