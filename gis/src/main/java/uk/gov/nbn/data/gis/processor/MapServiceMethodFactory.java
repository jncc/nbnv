package uk.gov.nbn.data.gis.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The following is a factory for obtaining a MapServiceMethod for a given 
 * requested path
 * @author Christopher Johnson
 */
@Component
public class MapServiceMethodFactory {
    @Autowired ApplicationContext context;
    @Autowired Properties properties;
    private MapServicePart rootMapService;
    private Collection<? extends Provider> providers;
    private Configuration config;
    private File templateDirectory;
    
    @PostConstruct public void init() {
        config = new Configuration();
        rootMapService = getMapCreatingMethods();
        providers = context.getBeansOfType(Provider.class).values();
    }
    
    
    public URL getMapServiceURL(File mapFile, String query) throws MalformedURLException {
        StringBuilder toReturn = new StringBuilder(properties.getProperty("mapserver"))
                .append("?map=")
                .append(URLEncoder.encode(mapFile.getAbsolutePath()));
        if(query != null) {
            toReturn.append("&").append(query);
        }
        return new URL(toReturn.toString());
    }
    
    /**
     * Adapt the template folder setting method of the Freemarker configuration
     * @param mapTemplates Folder for the map templates
     * @throws IOException If an issues occurs in setting the template folder
     */
    public void setMapTemplateDirectory(File templateDirectory) throws IOException {
        this.templateDirectory = templateDirectory;
        config.setDirectoryForTemplateLoading(templateDirectory);
    }
    
    File getMapFile(MapObject annotation, Map model) throws IOException, TemplateException {
        Template template = config.getTemplate(annotation.map());
        // File output
        File file = File.createTempFile("generated", ".map", templateDirectory);
        Writer out = new FileWriter (file);
        
        template.process(model, out);
        out.flush();
        out.close();
        return file;
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
    private MapServicePart getMapCreatingMethods() {
        MapServicePart rootNode = new MapServicePart(null, "");         
        
        for(Object mapServiceInstance : context.getBeansWithAnnotation(MapService.class).values()) {
            Class<?> currClass = mapServiceInstance.getClass();
            MapService classAnnot = currClass.getAnnotation(MapService.class);
            for(Method currMethod : currClass.getMethods()) {
                MapObject mapService = currMethod.getAnnotation(MapObject.class);
                if(mapService != null) {
                    //combine all of the parts together to create a path
                    String mapServiceFullName = classAnnot.value() + "/" + mapService.path();
                    
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
}
