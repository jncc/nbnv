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
    private String taxonGroupKey;
    private List<TaxonGroup> children;
    
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
}
