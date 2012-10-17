package uk.org.nbn.nbnv.api.model;

public class Taxon implements Comparable<Taxon>{
    
    private String taxonVersionKey;
    private String pTaxonVersionKey;
    private String name;
    private String authority;
    private String languageKey;
    private String taxonOutputGroupKey;
    
    public Taxon(){}
    
    public Taxon(String taxonVersionKey, String pTaxonVersionKey, String name, String authority, String languageKey, String taxonOutputGroupKey){
        super();
        this.taxonVersionKey = taxonVersionKey;
        this.pTaxonVersionKey = pTaxonVersionKey;
        this.name = name;
        this.authority = authority;
        this.languageKey = languageKey;
        this.taxonOutputGroupKey = taxonOutputGroupKey;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getPTaxonVersionKey() {
        return pTaxonVersionKey;
    }

    public void setPTaxonVersionKey(String pTaxonVersionKey) {
        this.pTaxonVersionKey = pTaxonVersionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTaxonOutputGroupKey() {
        return taxonOutputGroupKey;
    }

    public void setTaxonOutputGroupKey(String taxonOutputGroupKey) {
        this.taxonOutputGroupKey = taxonOutputGroupKey;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    @Override
    public int compareTo(Taxon that) {
        return this.name.compareTo(that.name);
    }
}
