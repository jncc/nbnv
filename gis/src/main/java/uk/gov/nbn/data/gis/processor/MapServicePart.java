package uk.gov.nbn.data.gis.processor;

import uk.gov.nbn.data.gis.processor.atlas.AtlasGrade;
import uk.gov.nbn.data.gis.processor.atlas.AtlasGradeProcessor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.gov.nbn.data.gis.processor.atlas.EnableAtlasGrade;

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
    private EnableAtlasGrade atlasGrade;
    private List<AtlasGradeProcessor> atlasGradeProcessors;

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
        if(this.associatedMethod !=null) {
            throw new IllegalArgumentException("A method has already been registered "
                    + "to this part. Conflict between " + this.associatedMethod 
                    + " and " + associatedMethod);
        }
        this.associatedMethod = associatedMethod;
    }
    
    
    public void setAtlasGradeProcessors(List<AtlasGradeProcessor> atlasGradeProcessors) {
        this.atlasGradeProcessors = atlasGradeProcessors;
    }
    
    public void setAtlasGradeAnnotation(EnableAtlasGrade atlasGrade) {
        this.atlasGrade = atlasGrade;
    }
    
    public List<AtlasGradeProcessor> getAtlasGradeProcessors() {
        return atlasGradeProcessors;
    }
        
    public boolean isAtlasGrade() {
        return this.atlasGrade != null;
    }
    
    public EnableAtlasGrade getAtlasGradeAnnotation() {
        return this.atlasGrade;
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
