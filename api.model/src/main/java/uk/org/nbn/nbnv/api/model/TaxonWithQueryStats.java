package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxonWithQueryStats implements Comparable<TaxonWithQueryStats>{
    
    private String taxonVersionKey;
    private int querySpecificObservationCount;
    private Taxon taxon;
    private int minYear;
    private int maxYear;
    
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

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    @Override
    public int compareTo(TaxonWithQueryStats that) {
        return this.taxon.compareTo(that.taxon);
    }
    
}
