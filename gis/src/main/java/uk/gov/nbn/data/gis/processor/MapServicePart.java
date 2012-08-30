package uk.gov.nbn.data.gis.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The following is a utility class which helps to build up map service names
 * @author Christopher Johnson
 */
class MapServicePart {
    private final String name;
    private final boolean isVariable;
    private final List<MapServicePart> children;
    private final Object instance;
    private MapServicePart parent;
    private Method associatedMethod;

    MapServicePart(Object instance, String name) {
        this.instance = instance;
        this.name = name;
        this.isVariable = name.startsWith("{") && name.endsWith("}");
        this.children = new ArrayList<MapServicePart>();
    }

    public String getName() {
        return name;
    }
    
    public Method getAssociatedMethod() {
        return associatedMethod;
    }

    public void setAssociatedMethod(Method associatedMethod) {
        this.associatedMethod = associatedMethod;
    }

    public boolean hasMethod() {
        return associatedMethod != null;
    }

    public List<MapServicePart> getChildren() {
        return children;
    }
    
    public void addChild(MapServicePart child) {
        child.parent = this;
        children.add(child);
    }
    
    public MapServicePart getParent() {
        return parent;
    }
    
    public boolean hasParent() {
        return parent != null;
    }

    public boolean matches(String name) {
        return isVariable || name.equals(this.name);
    }
    
    private String getVariablePartName() {
        return name.substring(1, name.length()-1);
    }
    
    Map<String, String> getVariableParameterMappings(String[] realParams) {
        Map<String, String> toReturn = new HashMap<String, String>();
        
        MapServicePart currServicePart = this;
        int i = realParams.length;
        do{
            i--;
            if(currServicePart.isVariable) {
                toReturn.put(currServicePart.getVariablePartName(), realParams[i]);
            }
        }
        while((currServicePart = currServicePart.parent) != null);
        
        return toReturn;
    }

    Object getMapServiceInstance() {
        return instance;
    }
}
