/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

/**
 *
 * @author Administrator
 */
public class TaxonObservationAttribute {
    private String _key;
    private String _value;

    /**
     * @return the _key
     */
    public String getKey() {
        return _key;
    }

    /**
     * @return the _value
     */
    public String getValue() {
        return _value;
    }

    TaxonObservationAttribute(String key, String value) {
        this._key = key;
        this._value = value;
    }
}

