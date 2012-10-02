package uk.gov.nbn.data.gis.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.gov.nbn.data.gis.processor.MapServiceMethod.Type;

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
    private Type mapServiceType;

    MapServicePart(Object instance, String name) {
        this.instance = instance;
        this.name = name;
        this.isVariable = name.startsWith("{") && name.endsWith("}");
        this.children = new ArrayList<MapServicePart>();
        this.mapServiceType = Type.STANDARD;
    }

    String getName() {
        return name;
    }
    
    void setMapServiceType(Type mapServiceType) {
        this.mapServiceType = mapServiceType;
    }
    
    Type getMapServiceType() {
        return mapServiceType;
    }
    
    Method getAssociatedMethod() {
        return associatedMethod;
    }

    void setAssociatedMethod(Method associatedMethod) {
        if(this.associatedMethod !=null) {
            throw new IllegalArgumentException("A method has already been registered "
                    + "to this part. Conflict between " + this.associatedMethod 
                    + " and " + associatedMethod);
        }
        this.associatedMethod = associatedMethod;
    }

    boolean hasMethod() {
        return associatedMethod != null;
    }

    List<MapServicePart> getChildren() {
        return children;
    }
    
    void addChild(MapServicePart child) {
        child.parent = this;
        children.add(child);
    }
    
    MapServicePart getParent() {
        return parent;
    }
    
    boolean hasParent() {
        return parent != null;
    }

    boolean matches(String name) {
        return isVariable || name.equals(this.name);
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

    private String getVariablePartName() {
        return name.substring(1, name.length()-1);
    }
}
