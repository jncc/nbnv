/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.siteboundary;

import nbn.common.feature.Feature;
import nbn.common.feature.FeatureType;

/**
 *
 * @author Administrator
 */
public class SiteBoundary extends Feature {
    private String _providerKey;
    private String _siteKey;
    private String _siteName;
    private String _siteType;
    private String _layerName;
    private int _id;

    /**
     * @return the _providerKey
     */
    public String getProviderKey() {
        return _providerKey;
    }

    /**
     * @return the _siteKey
     */
    public String getSiteKey() {
        return _siteKey;
    }

    /**
     * @return the _siteName
     */
    public String getSiteName() {
        return _siteName;
    }

    /**
     * @return the _siteType
     */
    public String getSiteType() {
        return _siteType;
    }

    public String getLayerName() {
        return this._layerName;
    }

    SiteBoundary(int id, String providerKey, String siteKey, String siteName, String siteType, String layerName) {
        this._providerKey = providerKey;
        this._siteKey = siteKey;
        this._siteName = siteName;
        this._siteType = siteType;
        this._layerName = layerName;
        this._id = id;
    }

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }
    
    @Override
    public String getName() {
        return _siteName;
    }

    @Override
    public String getUniqueIDForFeatureType() {
        return Integer.toString(getId());
    }

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.SITEBOUNDARY;
    }
}
