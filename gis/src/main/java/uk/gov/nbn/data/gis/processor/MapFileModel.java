package uk.gov.nbn.data.gis.processor;

import java.util.Map;

/**
 * The following is the object which is to be returned by the map classes
 * in order to generate map files to pass to mapserver
 * @author Christopher Johnson
 */
public class MapFileModel {
    private final String mapFile;
    private final Map model;
    
    public MapFileModel(String mapFile, Map model) {
        this.mapFile = mapFile;
        this.model = model;
    }

    public String getMapFile() {
        return mapFile;
    }

    public Map getModel() {
        return model;
    }
}
