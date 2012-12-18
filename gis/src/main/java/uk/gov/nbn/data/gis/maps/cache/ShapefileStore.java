package uk.gov.nbn.data.gis.maps.cache;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The following component is a store of all the shape files which we hold in the
 * vector directory of the Context layers
 * @author cjohn
 */
@Component
public class ShapefileStore {
    private final Map<String, File> shapefiles;
    
    @Autowired 
    public ShapefileStore(Properties properties) {
        File shapefileList = new File(properties.getProperty("contextLayersLocation"), "Vector");
        this.shapefiles = new HashMap<String, File>();
        for(File currShapefile : shapefileList.listFiles(new ShapefileFilter())) {
            shapefiles.put(getShapefileName(currShapefile), currShapefile);
        }
    }
    
    public boolean isShapefilePresent(String shapefileName) {
        return shapefiles.containsKey(shapefileName);
    }
    
    public File getShapefile(String shapefileName) {
        return shapefiles.get(shapefileName);
    }
    
    private static String getShapefileName(File shapefile) {
        String shapefileName = shapefile.getName();
        return shapefileName.substring(0, shapefileName.length() - 4);
    }
    
    private static class ShapefileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".shp");
        }
    }
}
