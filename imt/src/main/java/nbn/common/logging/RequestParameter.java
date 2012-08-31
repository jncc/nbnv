/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.logging;

/**
 *
 * @author Administrator
 */
public class RequestParameter {
    private String _key;
    private String _value;

    /**
     * @return the _key
     */
    public String getKey() {
        return _key;
    }

    /**
     * @param key the _key to set
     */
    public void setKey(String key) {
        this._key = key;
    }

    /**
     * @return the _value
     */
    public String getValue() {
        return _value;
    }

    /**
     * @param value the _value to set
     */
    public void setValue(String value) {
        this._value = value;
    }

    public RequestParameter(String key, String value) {
        this._key = key;
        this._value = value;
    }
}
