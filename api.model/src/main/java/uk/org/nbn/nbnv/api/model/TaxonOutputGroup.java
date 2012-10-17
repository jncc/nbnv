package uk.org.nbn.nbnv.api.model;

public class TaxonOutputGroup {
    
    private String key, name, description, parentTaxonGroupKey;
    private int sortOrder;
    
    public TaxonOutputGroup(){}
    
    public TaxonOutputGroup(String key, String name, String description, String parentTaxonGroupKey, int sortOrder){
        this.key = key;
        this.sortOrder = sortOrder;
        this.name = name;
        this.description = description;
        this.parentTaxonGroupKey = parentTaxonGroupKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
}
