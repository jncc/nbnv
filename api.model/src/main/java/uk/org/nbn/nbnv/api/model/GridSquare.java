package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Christopher Johnson
 */
public class GridSquare {
    private String name, projection, resolution, parentSquare;
    private BoundingBox boundingBox;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getParentSquare() {
        return parentSquare;
    }

    public void setParentSquare(String parentSquare) {
        this.parentSquare = parentSquare;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}
