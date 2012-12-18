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
    private final Map<String, Map<String, File>> shapefiles;
    
    @Autowired 
    public ShapefileStore(Properties properties) {
        File shapefileList = new File(properties.getProperty("contextLayersLocation"), "Vector");
                
        this.shapefiles = new HashMap<String, Map<String, File>>();
        for(File shapefile : shapefileList.listFiles(new DirectoryFilter())) {
            Map<String, File> shapefileInProjection = new HashMap<String, File>();
            
            for(File currShapefile : shapefile.listFiles(new ShapefileFilter())) {
                shapefileInProjection.put(getShapefileName(currShapefile), currShapefile);
            }
            
            shapefiles.put(shapefile.getName(), shapefileInProjection);
        }        
    }
    
    public boolean isShapefilePresent(String shapefileName) {
        return shapefiles.containsKey(shapefileName);
    }
    
    public Map<String, File> getProjections(String shapefileName) {
        return shapefiles.get(shapefileName);
    }
    
    private static String getShapefileName(File shapefile) {
        String shapefileName = shapefile.getName();
        return "EPSG:" + shapefileName.substring(0, shapefileName.length() - 4);
    }
    
    private static class ShapefileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".shp");
        }
    }
    
    private static class DirectoryFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    }
}
