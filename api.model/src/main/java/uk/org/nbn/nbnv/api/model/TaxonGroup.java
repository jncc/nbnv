/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class TaxonGroup {
    private int sortOrder;
    private String taxonGroupName;
    private String descriptor;
    private String parent;
    private String taxonGroupKey;
    private int numSpecies;
    private List<TaxonGroup> children;

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

    public String getTaxonGroupName() {
        return taxonGroupName;
    }

    public void setTaxonGroupName(String taxonGroupName) {
        this.taxonGroupName = taxonGroupName;
    }
    
    public String getTaxonGroupKey() {
        return taxonGroupKey;
    }
    
    public void setTaxonGroupKey(String taxonGroupKey) {
        this.taxonGroupKey = taxonGroupKey;
    }
    
    public List<TaxonGroup> getChildren() {
        return children;
    }
        
    public void setChildren(List<TaxonGroup> children) {
        this.children = children;
    }

    public int getNumSpecies() {
        return numSpecies;
    }

    public void setNumSpecies(int numSpecies) {
        this.numSpecies = numSpecies;
    }
}
