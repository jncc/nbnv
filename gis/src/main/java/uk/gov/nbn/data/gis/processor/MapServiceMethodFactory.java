package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;

/**
 * The following is a factory for obtaining a MapServiceMethod for a given 
 * requested path
 * @author Christopher Johnson
 */
public class MapServiceMethodFactory {   
    private final MapServicePart rootMapService;
    private final List<? extends Provider> providers;
    
    /**
     * Create a map service part factory which loads maps and providers from
     * the respectively provided package names
     * @param mapsPackage The maps Package to load from
     * @param providersPackage The providers Package to load from
     * @throws InstantiationException If a loaded provider or map can not be instantiated
     * @throws IllegalAccessException If a loaded provider of map can not be accesses
     */
    public MapServiceMethodFactory(String mapsPackage, String providersPackage) throws InstantiationException, IllegalAccessException {
        rootMapService = getMapCreatingMethods(mapsPackage);
        providers = getProviders(providersPackage);
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
            return new MapServiceMethod(matchingPart, requestedParts, this);
        }
        else {
            throw new MapServiceUndefinedException("There is no MapServicePart which matches the path supplied");
        }
    }
    
    /**
     * Resolves a MapServiceMethod parameter from one of the providers in the 
     * providers package
     * @param method
     * @param request
     * @param toReturn
     * @param paramAnnotations
     * @return A instantiated Object from a provider in the provider class
     * @throws ProviderException 
     */
    Object getProvidedForParameter(MapServiceMethod method, HttpServletRequest request, Class<?> toReturn, List<Annotation> paramAnnotations) throws ProviderException {
        for(Provider currProvider : providers) {
            if(currProvider.isProviderFor(toReturn, method, request, paramAnnotations)) {
                return currProvider.provide(toReturn, method, request, paramAnnotations);
            }
        }
        return null; //can't find a matching parameter
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
    private static MapServicePart getMapCreatingMethods(final String packageLoc) throws InstantiationException, IllegalAccessException {
        MapServicePart rootNode = new MapServicePart(null, "");  
        ComponentScanner scanner = new ComponentScanner();
        
        Set<Class<?>> classes = scanner.getClasses(new ComponentQuery() {
            @Override protected void query() {
                select().from(packageLoc).returning(
                    allAnnotatedWith(MapService.class));
            }
        });
        
        for(Class<?> currClass : classes) {
            Object mapServiceInstance = currClass.newInstance();
            MapService classAnnot = currClass.getAnnotation(MapService.class);
            for(Method currMethod : currClass.getMethods()) {
                MapObject mapService = currMethod.getAnnotation(MapObject.class);
                if(mapService != null) {
                    //combine all of the parts together to create a path
                    String mapServiceFullName = classAnnot.value() + "/" + mapService.value();
                    
                    Iterator<String> partNameIterator = Arrays.asList(mapServiceFullName.split("/")).iterator();
                    MapServicePart pathPartOrCreate = getPathPartOrCreate(mapServiceInstance, partNameIterator.next(), rootNode);

                    while(partNameIterator.hasNext()) {
                        pathPartOrCreate = getPathPartOrCreate(mapServiceInstance, partNameIterator.next(), pathPartOrCreate);
                    }
                    pathPartOrCreate.setAssociatedMethod(currMethod);
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
    
    /* Load and instatiate a list of providers from a providers package */
    private static List<Provider> getProviders(final String providersPackage) throws InstantiationException, IllegalAccessException {
        final List<Provider> providerInstances = new ArrayList<Provider>();
        final Set<Class<? extends Provider>> providerClasses = new HashSet<Class<? extends Provider>>();
        
        ComponentScanner scanner = new ComponentScanner();
        scanner.getClasses(new ComponentQuery() {
            @Override protected void query() {
                select().from(providersPackage).andStore(
                    thoseImplementing(Provider.class).into(providerClasses));
            }
        });

        for(Class<? extends Provider> currProviderClass : providerClasses) {
            providerInstances.add(currProviderClass.newInstance());
        }
        return providerInstances;
    }
}
