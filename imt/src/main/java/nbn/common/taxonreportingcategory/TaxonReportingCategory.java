/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxonreportingcategory;

/**
 *
 * @author Administrator
 */
public class TaxonReportingCategory {
    private String _name;
    private String _nbnTaxonKey;
    private int _trcKey;

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @return the _nbnTaxonKey
     */
    public String getNbnTaxonKey() {
        return _nbnTaxonKey;
    }

    /**
     * @return the _trcKey
     */
    public int getTrcKey() {
        return _trcKey;
    }

    TaxonReportingCategory(int key, String nbnKey, String name) {
        this._name = name;
        this._nbnTaxonKey = nbnKey;
        this._trcKey = key;


    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaxonReportingCategory) {
            if (((TaxonReportingCategory)obj).getNbnTaxonKey().equals(this.getNbnTaxonKey()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this._nbnTaxonKey != null ? this._nbnTaxonKey.hashCode() : 0);
        return hash;
    }
}
