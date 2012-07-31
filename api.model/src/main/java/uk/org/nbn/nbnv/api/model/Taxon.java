package uk.org.nbn.nbnv.api.model;

public class Taxon {
    
    private String taxonVersionKey;
    private String prefnameTaxonVersionKey;
    private String taxonName;
    private String taxonAuthority;
    private String lang;
    private String taxonOutputGroupKey;
    private String taxonNavigationGroupKey;
    
    public Taxon(){}
    
    public Taxon(String taxonVersionKey, String prefnameTaxonVersionKey, String taxonName, String taxonAuthority, String lang, String taxonOutputGroupKey, String taxonNavigationGroupKey){
        super();
        this.taxonVersionKey = taxonVersionKey;
        this.prefnameTaxonVersionKey = prefnameTaxonVersionKey;
        this.taxonName = taxonName;
        this.taxonAuthority = taxonAuthority;
        this.lang = lang;
        this.taxonOutputGroupKey = taxonOutputGroupKey;
        this.taxonNavigationGroupKey = taxonNavigationGroupKey;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getPrefnameTaxonVersionKey() {
        return prefnameTaxonVersionKey;
    }

    public void setPrefnameTaxonVersionKey(String prefnameTaxonVersionKey) {
        this.prefnameTaxonVersionKey = prefnameTaxonVersionKey;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    public String getTaxonAuthority() {
        return taxonAuthority;
    }

    public void setTaxonAuthority(String taxonAuthority) {
        this.taxonAuthority = taxonAuthority;
    }

    public String getTaxonOutputGroupKey() {
        return taxonOutputGroupKey;
    }

    public void setTaxonOutputGroupKey(String taxonOutputGroupKey) {
        this.taxonOutputGroupKey = taxonOutputGroupKey;
    }

    public String getTaxonNavigationGroupKey() {
        return taxonNavigationGroupKey;
    }

    public void setTaxonNavigationGroupKey(String taxonNavigationGroupKey) {
        this.taxonNavigationGroupKey = taxonNavigationGroupKey;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}