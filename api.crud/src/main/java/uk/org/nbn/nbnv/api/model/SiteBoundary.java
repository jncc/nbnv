package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
public class SiteBoundary {
    
    @NotEmpty
    private int featureID;
    private String name;
    @NotEmpty
    private String providerKey;
    private String description;
    @NotEmpty
    private String siteBoundaryDatasetKey;
    @NotEmpty
    private int siteBoundaryCategoryId;
    @NotEmpty
    private String identifier;
    private SiteBoundaryDataset siteBoundaryDataset;
    private SiteBoundaryCategory siteBoundaryCategory;
    
    public SiteBoundary(){}
    
    public SiteBoundary(int featureID, String name, String providerKey, String description, String siteBoundaryDatasetKey, int siteBoundaryCategoryId, String identifier){
        super();
        this.featureID = featureID;
        this.name = name;
        this.providerKey = providerKey;
        this.description = description;
        this.siteBoundaryDatasetKey = siteBoundaryDatasetKey;
        this.siteBoundaryCategoryId = siteBoundaryCategoryId;
        this.identifier = identifier;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getSiteBoundaryDatasetKey() {
        return siteBoundaryDatasetKey;
    }

    public void setSiteBoundaryDatasetKey(String siteBoundaryDatasetKey) {
        this.siteBoundaryDatasetKey = siteBoundaryDatasetKey;
    }

    public int getSiteBoundaryCategoryId() {
        return siteBoundaryCategoryId;
    }

    public void setSiteBoundaryCategoryId(int siteBoundaryCategoryId) {
        this.siteBoundaryCategoryId = siteBoundaryCategoryId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SiteBoundaryDataset getSiteBoundaryDataset() {
        return siteBoundaryDataset;
    }

    public void setSiteBoundaryDataset(SiteBoundaryDataset siteBoundaryDataset) {
        this.siteBoundaryDataset = siteBoundaryDataset;
    }

    public SiteBoundaryCategory getSiteBoundaryCategory() {
        return siteBoundaryCategory;
    }

    public void setSiteBoundaryCategory(SiteBoundaryCategory siteBoundaryCategory) {
        this.siteBoundaryCategory = siteBoundaryCategory;
    }
    
}