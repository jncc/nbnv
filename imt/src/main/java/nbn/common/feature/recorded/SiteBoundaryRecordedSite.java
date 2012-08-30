
package nbn.common.feature.recorded;

import nbn.common.siteboundary.SiteBoundary;

public class SiteBoundaryRecordedSite implements RecordedSite<SiteBoundary> {
    private SiteBoundary site;
    
    public SiteBoundaryRecordedSite(SiteBoundary site) {
        this.site = site;
    }

    public SiteBoundary getUnderlyingFeature() {
        return site;
    }
}
