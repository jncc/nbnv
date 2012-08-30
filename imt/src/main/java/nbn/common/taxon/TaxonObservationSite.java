/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

import nbn.common.feature.Feature;
import nbn.common.feature.recorded.RecordedSite;

/**
 *
 * @author Administrator
 */

//public Enum TORefSys {

//}
public class TaxonObservationSite<S extends RecordedSite<? extends Feature>> {
    private int _locID;
    private String _siteName;
    private String _providerLocID;
    private S recordedSite;

    public S getRecordedSite() {
        return recordedSite;
    }
    
    /**
     * @return the _locID
     */
    public int getLocID() {
        return _locID;
    }

    /**
     * @return the _siteName
     */
    public String getSiteName() {
        return _siteName;
    }

    /**
     * @return the _providerLocID
     */
    public String getProviderLocID() {
        return _providerLocID;
    }

    void setRecordedSite(S recordedSite) {
        this.recordedSite = recordedSite;
    }

    /**
     * @param siteName the _siteName to set
     */
    void setSiteName(String siteName) {
        this._siteName = siteName;
    }

    /**
     * @param providerLocID the _providerLocID to set
     */
    void setProviderLocID(String providerLocID) {
        this._providerLocID = providerLocID;
    }

    TaxonObservationSite(int locationID) {
        this._locID = locationID;
    }
}
