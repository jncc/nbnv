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
    public MapServiceMethod getMapServiceMethod(String pathInfo) throws MapServiceUndefinedException {
        return getMapServiceMethod(pathInfo.substring(1).split("/"));        
    }
    
    /**
     * Obtain a map service method for a given pathInfo array
     * @param requestedParts The path info string split into parts
     * @return A map method which corresponds to this pathInfo
     * @throws MapServiceUndefinedException If no map can be resolved to this path
     */
    public MapServiceMethod getMapServiceMethod(String[] requestedParts) throws MapServiceUndefinedException {
        MapServicePart matchingPart = getMatchingPart(rootMapService, 0, requestedParts);
        if(matchingPart != null && matchingPart.hasMethod()) {
            return new MapServiceMethod(matchingPart, providerFactory);
        }
        else {
            throw new MapServiceUndefinedException("There is no MapServicePart which matches the path supplied");
        }
    }
    
    /**
     * The following method will recurse the root MapServicePart to return a 
     * complete list of the MapServiceMethods registered in this factory
     * @return A list of mapService methods registerd to the factory
     */
    public List<MapServiceMethod> getMapServiceMethodsRegistered() {
        return getMapServiceMethods(rootMapService);
    }
    
    /**
     * Recurse from the root to find all map service parts
     * @param partToLookIn
     * @return A list of the map services registered at this part
     */
    private List<MapServiceMethod> getMapServiceMethods(MapServicePart partToLookIn) {
        List<MapServiceMethod> toReturn = new ArrayList<MapServiceMethod>();
        if(partToLookIn.hasMethod()) {
            toReturn.add(new MapServiceMethod(partToLookIn, providerFactory));
        }
        for(MapServicePart currMethod : partToLookIn.getChildren()) {
            toReturn.addAll(getMapServiceMethods(currMethod));
        }
        return toReturn;
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
                //check to see if this part matches the given part in the request
                if(currPart.matches(requestedParts[index])) { 
                    //Yay, this part matches. Traverse down this part (RECURSE)
                    MapServicePart prospectivePart = getMatchingPart(currPart, index+1, requestedParts);
                    if(prospectivePart != null) { //check to see if this part was successful. If not, try next
                        return prospectivePart;
                    }
                }
            }
            return null; //failed to find anything
        }
    }
                
    /* Load and all of the maps and return the root mapservicepart */
    private MapServicePart getMapCreatingMethods() {
        MapServicePart rootNode = new MapServicePart("");         
        
        for(Object mapServiceInstance : context.getBeansWithAnnotation(MapContainer.class).values()) {
            Class<?> currClass = mapServiceInstance.getClass();
            MapContainer classAnnot = currClass.getAnnotation(MapContainer.class);
            for(Method currMethod : currClass.getMethods()) {
                MapService mapService = currMethod.getAnnotation(MapService.class);
                if(mapService != null) {
                    //combine all of the parts together to create a path
                    String mapServiceFullName = classAnnot.value() + "/" + mapService.value();
                    
                    Iterator<String> partNameIterator = Arrays.asList(mapServiceFullName.split("/")).iterator();
                    MapServicePart pathPartOrCreate = getPathPartOrCreate(partNameIterator.next(), rootNode);

                    while(partNameIterator.hasNext()) {
                        pathPartOrCreate = getPathPartOrCreate(partNameIterator.next(), pathPartOrCreate);
                    }
                    pathPartOrCreate.setAssociatedMethodAndInstance(mapServiceInstance, currMethod);
                    
                    //register none standard map service functionality
                    for(Type mapServiceType : Type.getTypesValidForMethod(currMethod)) {
                        MapServicePart gridMapServicePart = getPathPartOrCreate(mapServiceType.getRequest(), pathPartOrCreate);
                        gridMapServicePart.setAssociatedMethodAndInstance(mapServiceInstance, currMethod);
                        gridMapServicePart.setMapServiceType(mapServiceType);
                    }
                }
            }
        }
        return rootNode;
    }
    
    private static MapServicePart getPathPartOrCreate(String name, MapServicePart toFindIn) {
        //look for a child which has the same name as the passed in name
        for(MapServicePart currChild : toFindIn.getChildren()) {
           if(name.equals(currChild.getName())) {
               return currChild;
           }
        }
        //Could not find. Create a new one
        MapServicePart newPathPart = new MapServicePart(name);
        toFindIn.addChild(newPathPart);
        return newPathPart;
    }
}
