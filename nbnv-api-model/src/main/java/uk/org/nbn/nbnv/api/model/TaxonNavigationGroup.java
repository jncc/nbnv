package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxonNavigationGroup {
    private String key;
    private int sortOrder;
    private String name;
    private String description;
    private String parentTaxonGroupKey;
    private int numSpecies;
    private List<TaxonNavigationGroup> children;

    public String getKey(){
        return key;
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentTaxonGroupKey() {
        return parentTaxonGroupKey;
    }

    public void setParentTaxonGroupKey(String parentTaxonGroupKey) {
        this.parentTaxonGroupKey = parentTaxonGroupKey;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<TaxonNavigationGroup> getChildren() {
        return children;
    }
        
    public void setChildren(List<TaxonNavigationGroup> children) {
        this.children = children;
    }

    public int getNumSpecies() {
        return numSpecies;
    }

    public void setNumSpecies(int numSpecies) {
        this.numSpecies = numSpecies;
    }
}
