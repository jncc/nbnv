package uk.gov.nbn.data.gis.processor;

import java.lang.reflect.Method;
import java.util.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;

/**
 * The following is a factory for obtaining a MapServiceMethod for a given 
 * requested path
 * @author Christopher Johnson
 */
@Component
public class MapServiceMethodFactory {
    @Autowired ApplicationContext context;
    @Autowired ProviderFactory providerFactory;
    private MapServicePart rootMapService;
    
    @PostConstruct public void init() {
        rootMapService = getMapCreatingMethods();
    }
    
    /**
     * Obtain a map service method for a give pathInfo String
     * @param pathInfo The path info string
     * @return A map method which corresponds to this pathInfo
     * @throws MapServiceUndefinedException If no map can be resolved to this path
     */
    public MapServiceMethod getMatchingPart(String pathInfo) throws MapServiceUndefinedException {
        return getMatchingPart(pathInfo.substring(1).split("/"));        
    }
    
    /**
     * Obtain a map service method for a give pathInfo String
     * @param requestedParts The path info string split into parts
     * @return A map method which corresponds to this pathInfo
     * @throws MapServiceUndefinedException If no map can be resolved to this path
     */
    public MapServiceMethod getMatchingPart(String[] requestedParts) throws MapServiceUndefinedException {
        MapServicePart matchingPart = getMatchingPart(rootMapService, 0, requestedParts);
        if(matchingPart != null && matchingPart.hasMethod()) {
            return new MapServiceMethod(matchingPart, requestedParts, providerFactory);
        }
        else {
            throw new MapServiceUndefinedException("There is no MapServicePart which matches the path supplied");
        }
    }
    
    /**
     * Recurse from the root for the specified path
     * @param from
     * @param index
     * @param requestedParts
     * @return Found part or null
     */
    private static MapServicePart getMatchingPart(MapServicePart from, int index, String[] requestedParts) {
        if(index == requestedParts.length) {
            return from; //found the right part
        }
        else {
            for(MapServicePart currPart :from.getChildren()) {
                if(currPart.matches(requestedParts[index])) {
                    return getMatchingPart(currPart, index+1, requestedParts);
                }
            }
            return null; //failed to find anything
        }
    }
                
    /* Load and all of the maps and return the root mapservicepart */
    private MapServicePart getMapCreatingMethods() {
        MapServicePart rootNode = new MapServicePart(null, "");         
        
        for(Object mapServiceInstance : context.getBeansWithAnnotation(MapContainer.class).values()) {
            Class<?> currClass = mapServiceInstance.getClass();
            MapContainer classAnnot = currClass.getAnnotation(MapContainer.class);
            for(Method currMethod : currClass.getMethods()) {
                MapService mapService = currMethod.getAnnotation(MapService.class);
                if(mapService != null) {
                    //combine all of the parts together to create a path
                    String mapServiceFullName = classAnnot.value() + "/" + mapService.value();
                    
                    Iterator<String> partNameIterator = Arrays.asList(mapServiceFullName.split("/")).iterator();
                    MapServicePart pathPartOrCreate = getPathPartOrCreate(mapServiceInstance, partNameIterator.next(), rootNode);

                    while(partNameIterator.hasNext()) {
                        pathPartOrCreate = getPathPartOrCreate(mapServiceInstance, partNameIterator.next(), pathPartOrCreate);
                    }
                    pathPartOrCreate.setAssociatedMethod(currMethod);
                    
                    //register atlasgrade functionality
                    AtlasGrade atlasGradeAnnotation = currMethod.getAnnotation(AtlasGrade.class);
                    if(atlasGradeAnnotation != null) {
                        for(Type mapServiceType : EnumSet.complementOf(EnumSet.of(Type.STANDARD))) {
                            MapServicePart atlasGradeMapServicePart = getPathPartOrCreate(mapServiceInstance, mapServiceType.getRequest(), pathPartOrCreate);
                            atlasGradeMapServicePart.setAssociatedMethod(currMethod);
                            atlasGradeMapServicePart.setMapServiceType(mapServiceType);
                        }
                    }
                }
            }
        }
        return rootNode;
    }
    
    private static MapServicePart getPathPartOrCreate(Object instance, String name, MapServicePart toFindIn) {
        MapServicePart potentialNewPathPart = new MapServicePart(instance, name);
        List<MapServicePart> list = toFindIn.getChildren();
        int indexOfPathPart = list.indexOf(potentialNewPathPart);
        if(indexOfPathPart != -1) {
            return list.get(indexOfPathPart);
        }
        else {
            toFindIn.addChild(potentialNewPathPart);
            return potentialNewPathPart;
        }
    }
}
