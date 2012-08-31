
package nbn.common.feature;

public class GridSquare extends Feature {
    private Resolution resolution;
    private String gridRef;
    private GridSystem gridSystem;
    private int minEasting, minNorthing;
    
    public GridSquare(String gridRef) {
        this.gridRef = gridRef;
        this.gridSystem = GridSystem.getGridSystemByGridRef(gridRef);
        this.resolution = Resolution.getResolutionByGridRef(gridRef, gridSystem);
    }
    
    public GridSquare(String gridRef, int minEasting, int minNorthing) {
        this(gridRef);
        this.minEasting = minEasting;
        this.minNorthing = minNorthing; 
    }

    public String getGridRef() {
        return gridRef;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public GridSystem getGridSystem() {
        return gridSystem;
    }

    @Override
    public String getName() {
        return gridRef;
    }

    @Override
    public String getUniqueIDForFeatureType() {
        return gridRef;
    }

    @Override
    public FeatureType getFeatureType() {
        return FeatureType.GRIDSQUARE;
    }

    public int getMinEasting() {
        return minEasting;
    }

    public int getMinNorthing() {
        return minNorthing;
    }

    public int getMaxEasting() {
        return minEasting + resolution.getAccuracyInMetres();
    }

    public int getMaxNorthing() {
        return minNorthing + resolution.getAccuracyInMetres();
    }
}
