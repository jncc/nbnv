package uk.org.nbn.nbnv.api.model;

public class TaxonOutputGroup {
    
    private String taxonGroupKey, taxonGroupName, descriptor, parent;
    private int sortOrder;
    
    public TaxonOutputGroup(){}
    
    public TaxonOutputGroup(String taxonGroupKey, String taxonGroupName, String descriptor, String parent, int sortOrder){
        this.taxonGroupKey = taxonGroupKey;
        this.taxonGroupName = taxonGroupName;
        this.descriptor = descriptor;
        this.parent = parent;
        this.sortOrder = sortOrder;
    }

    public String getTaxonGroupKey() {
        return taxonGroupKey;
    }

    public void setTaxonGroupKey(String taxonGroupKey) {
        this.taxonGroupKey = taxonGroupKey;
    }

    public String getTaxonGroupName() {
        return taxonGroupName;
    }

    public void setTaxonGroupName(String taxonGroupName) {
        this.taxonGroupName = taxonGroupName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
}
