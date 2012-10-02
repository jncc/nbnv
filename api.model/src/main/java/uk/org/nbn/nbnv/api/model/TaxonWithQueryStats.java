package uk.org.nbn.nbnv.api.model;

public class TaxonWithQueryStats implements Comparable<TaxonWithQueryStats>{
    
    private String taxonVersionKey;
    private int querySpecificObservationCount;
    private Taxon taxon;
    
    public TaxonWithQueryStats(){}
    
    public TaxonWithQueryStats(String taxonVersionKey, int querySpecificObservationCount, Taxon taxon){
        this.taxonVersionKey = taxonVersionKey;
        this.querySpecificObservationCount = querySpecificObservationCount;
        this.taxon = taxon;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public int getQuerySpecificObservationCount() {
        return querySpecificObservationCount;
    }

    public void setQuerySpecificObservationCount(int querySpecificObservationCount) {
        this.querySpecificObservationCount = querySpecificObservationCount;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    @Override
    public int compareTo(TaxonWithQueryStats that) {
        return this.taxon.compareTo(that.taxon);
    }
    
}
