package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;
import java.net.URI;

public class Taxon implements Comparable<Taxon>{
    
    private String taxonVersionKey;
    private String pTaxonVersionKey;
    private String name;
    private String authority;
    private String languageKey;
    private String taxonOutputGroupKey;
    private String taxonOutputGroupName;
    private String commonNameTaxonVersionKey;
    private String commonName;
    private String organismKey;
    private String rank;
    private String nameStatus;
    private String versionForm;
    private long gatewayRecordCount;

    @Ref(value="${resource.portalUrl}/Taxa/${instance.taxonVersionKey}", style=Style.RELATIVE_PATH) 
    private URI href;

    public Taxon(){}
    
    public Taxon(String taxonVersionKey, String pTaxonVersionKey, String name, String authority, String languageKey, String taxonOutputGroupKey, String taxonOutputGroupName){
        super();
        this.taxonVersionKey = taxonVersionKey;
        this.pTaxonVersionKey = pTaxonVersionKey;
        this.name = name;
        this.authority = authority;
        this.languageKey = languageKey;
        this.taxonOutputGroupKey = taxonOutputGroupKey;
        this.taxonOutputGroupName = taxonOutputGroupName;
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

    public String getTaxonOutputGroupName() {
        return taxonOutputGroupName;
    }

    public void setTaxonOutputGroupName(String taxonOutputGroupName) {
        this.taxonOutputGroupName = taxonOutputGroupName;
    }

    /**
     * @return the commonNameTaxonVersionKey
     */
    public String getCommonNameTaxonVersionKey() {
        return commonNameTaxonVersionKey;
    }

    /**
     * @param commonNameTaxonVersionKey the commonNameTaxonVersionKey to set
     */
    public void setCommonNameTaxonVersionKey(String commonNameTaxonVersionKey) {
        this.commonNameTaxonVersionKey = commonNameTaxonVersionKey;
    }

    /**
     * @return the organismKey
     */
    public String getOrganismKey() {
        return organismKey;
    }

    /**
     * @param organismKey the organismKey to set
     */
    public void setOrganismKey(String organismKey) {
        this.organismKey = organismKey;
    }

    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @return the nameStatus
     */
    public String getNameStatus() {
        return nameStatus;
    }

    /**
     * @param nameStatus the nameStatus to set
     */
    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    /**
     * @return the versionForm
     */
    public String getVersionForm() {
        return versionForm;
    }

    /**
     * @param versionForm the versionForm to set
     */
    public void setVersionForm(String versionForm) {
        this.versionForm = versionForm;
    }

    /**
     * @return the commonName
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * @param commonName the commonName to set
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
    
    /**
     * @return The link to the portal version of this resource
     */
    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public long getGatewayRecordCount() {
        return gatewayRecordCount;
    }

    public void setGatewayRecordCount(long gatewayRecordCount) {
        this.gatewayRecordCount = gatewayRecordCount;
    }
    
    
}
