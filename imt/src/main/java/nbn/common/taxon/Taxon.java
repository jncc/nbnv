/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

import nbn.common.taxonreportingcategory.TaxonReportingCategory;

/**
 *
 * @author Administrator
 */
public class Taxon {
    private String name = "";
    private String taxonVersionKey = "";
    private int taxonKey;
    private String commonName = "";
    private TaxonReportingCategory taxonReportingCategory;
    private String authority = "";
    private String taxonRank = "";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the taxonVersionKey
     */
    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    /**
     * @return the taxonKey
     */
    public int getTaxonKey() {
        return taxonKey;
    }

    /**
     * @return the commonName
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * @return the taxonGroup
     */
    public String getTaxonGroup() {
        return taxonReportingCategory.getName();
    }

    public TaxonReportingCategory getTaxonReportingCategory() {
        return taxonReportingCategory;
    }

    /**
     * @return the authority
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * @param commonName the commonName to set
     */
    void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * @param taxonGroup the taxonGroup to set
     */
    void setTaxonReportingCategory(TaxonReportingCategory trc) {
        this.taxonReportingCategory = trc;
    }

    /**
     * @param authority the authority to set
     */
    void setAuthority(String authority) {
        this.authority = authority;
    }

    Taxon(int taxonKey, String taxonVersionKey) {
        this.taxonKey = taxonKey;
        this.taxonVersionKey = taxonVersionKey;

    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof Taxon) {
            if (this.taxonVersionKey.equals(((Taxon)obj).taxonVersionKey))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.taxonVersionKey != null ? this.taxonVersionKey.hashCode() : 0);
        return hash;
    }

    /**
     * @return the taxonRank
     */
    public String getTaxonRank() {
        return taxonRank;
    }

    /**
     * @param taxonRank the taxonRank to set
     */
    void setTaxonRank(String taxonRank) {
        this.taxonRank = taxonRank;
    }
}
