package uk.gov.nbn.data.gis;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Christopher Johnson
 */
public class MapServerMapFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return isMapFilename(name);
    }

    public static boolean isMapFilename(String name) {
        return name.endsWith(".map");
    }
    
    public static String getMapName(String name) {
        if(!isMapFilename(name)) {
            throw new IllegalArgumentException("The name does not represent a map filename");
        }
        return name.substring(0, name.length()-4);
    }
}
