package uk.org.nbn.nbnv.api.model;

public class TaxonCategory {
     
    private String taxonGroupId;
    private int sortOrder;
    private String name;
    private String description;
    private boolean isParent;
    
    public TaxonCategory(){}
    
    public TaxonCategory(String taxonGroupId, int sortOrder, String name, String description, boolean isParent){
        super();
        this.taxonGroupId = taxonGroupId;
        this.sortOrder = sortOrder;
        this.name = name;
        this.description = description;
        this.isParent = isParent;
    }

    public String getTaxonGroupId() {
        return taxonGroupId;
    }

    public void setTaxonGroupId(String taxonGroupId) {
        this.taxonGroupId = taxonGroupId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

}
