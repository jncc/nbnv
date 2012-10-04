package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Christopher Johnson
 */
public class Feature {
    private String label;
    private BoundingBox worldBoundingBox, nativeBoundingBox;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BoundingBox getWorldBoundingBox() {
        return worldBoundingBox;
    }

    public void setWorldBoundingBox(BoundingBox worldBoundingBox) {
        this.worldBoundingBox = worldBoundingBox;
    }

    public BoundingBox getNativeBoundingBox() {
        return nativeBoundingBox;
    }

    public void setNativeBoundingBox(BoundingBox nativeBoundingBox) {
        this.nativeBoundingBox = nativeBoundingBox;
    }
}
