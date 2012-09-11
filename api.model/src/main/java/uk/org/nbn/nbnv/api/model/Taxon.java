package uk.org.nbn.nbnv.api.model;

public class Taxon {
    
    private String taxonVersionKey;
    private String prefnameTaxonVersionKey;
    private String name;
    private String authority;
    private String lang;
    private String outputGroupKey;
    private String navigationGroupKey;
    private String outputGroupName;
    
    public Taxon(){}
    
    public Taxon(String taxonVersionKey, String prefnameTaxonVersionKey, String name, String authority, String lang, String outputGroupKey, String navigationGroupKey, String outputGroupName){
        super();
        this.taxonVersionKey = taxonVersionKey;
        this.prefnameTaxonVersionKey = prefnameTaxonVersionKey;
        this.name = name;
        this.authority = authority;
        this.lang = lang;
        this.outputGroupKey = outputGroupKey;
        this.navigationGroupKey = navigationGroupKey;
        this.outputGroupName = outputGroupName;
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

    public String getOutputGroupKey() {
        return outputGroupKey;
    }

    public void setOutputGroupKey(String outputGroupKey) {
        this.outputGroupKey = outputGroupKey;
    }

    public String getNavigationGroupKey() {
        return navigationGroupKey;
    }

    public void setNavigationGroupKey(String navigationGroupKey) {
        this.navigationGroupKey = navigationGroupKey;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOutputGroupName() {
        return outputGroupName;
    }

    public void setOutputGroupName(String outputGroupName) {
        this.outputGroupName = outputGroupName;
    }
}
