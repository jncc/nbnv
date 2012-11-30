package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import java.net.URI;

/**
 *
 * @author Christopher Johnson
 */
public class Feature {
    @Ref(value="${resource.portalUrl}/Reports/Sites/${instance.identifier}/Groups", style=Ref.Style.RELATIVE_PATH) 
    private URI href;
    
    private String label, identifier, type;
    private BoundingBox worldBoundingBox, nativeBoundingBox;

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
