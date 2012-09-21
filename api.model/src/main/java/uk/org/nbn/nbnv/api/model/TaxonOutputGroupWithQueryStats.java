package uk.org.nbn.nbnv.api.model;

public class TaxonOutputGroupWithQueryStats {
    
    private String taxonGroupKey;
    private int querySpecificSpeciesCount;
    private TaxonOutputGroup taxonOutputGroup;
    
    public TaxonOutputGroupWithQueryStats(){}
    
    public TaxonOutputGroupWithQueryStats(String taxonGroupKey, int querySpecificSpeciesCount, TaxonOutputGroup taxonOutputGroup){
        this.taxonGroupKey = taxonGroupKey;
        this.querySpecificSpeciesCount = querySpecificSpeciesCount;
        this.taxonOutputGroup = taxonOutputGroup;
    }

    public String getTaxonGroupKey() {
        return taxonGroupKey;
    }

    public void setTaxonGroupKey(String taxonGroupKey) {
        this.taxonGroupKey = taxonGroupKey;
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
