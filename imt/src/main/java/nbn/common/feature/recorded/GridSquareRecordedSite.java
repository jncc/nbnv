
package nbn.common.feature.recorded;

import nbn.common.feature.GridSquare;

public class GridSquareRecordedSite implements RecordedSite<GridSquare>, Blurrable{
    private GridSquare recordedGridRef;
    private boolean isBlurred;

    public GridSquareRecordedSite(GridSquare recordedGridRef, boolean isBlurred) {
        this.recordedGridRef = recordedGridRef;
        this.isBlurred = isBlurred;
    }

    public GridSquare getUnderlyingFeature() {
        return recordedGridRef;
    }

    public boolean isBlurred() {
        return isBlurred;
    }
}
