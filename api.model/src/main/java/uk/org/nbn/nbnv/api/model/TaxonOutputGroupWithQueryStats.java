package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxonOutputGroupWithQueryStats {
    
    private String taxonOutputGroupKey;
    private int querySpecificSpeciesCount;
    private TaxonOutputGroup taxonOutputGroup;
    
    public TaxonOutputGroupWithQueryStats(){}
    
    public TaxonOutputGroupWithQueryStats(String taxonOutputGroupKey, int querySpecificSpeciesCount, TaxonOutputGroup taxonOutputGroup){
        this.taxonOutputGroupKey = taxonOutputGroupKey;
        this.querySpecificSpeciesCount = querySpecificSpeciesCount;
        this.taxonOutputGroup = taxonOutputGroup;
    }

    public String getTaxonOutputGroupKey() {
        return taxonOutputGroupKey;
    }

    public void setTaxonOutputGroupKey(String taxonOutputGroupKey) {
        this.taxonOutputGroupKey = taxonOutputGroupKey;
    }

    public int getQuerySpecificSpeciesCount() {
        return querySpecificSpeciesCount;
    }

    public void setQuerySpecificSpeciesCount(int querySpecificSpeciesCount) {
        this.querySpecificSpeciesCount = querySpecificSpeciesCount;
    }

    public TaxonOutputGroup getTaxonOutputGroup() {
        return taxonOutputGroup;
    }

    public void setTaxonOutputGroup(TaxonOutputGroup taxonOutputGroup) {
        this.taxonOutputGroup = taxonOutputGroup;
    }
    
}
