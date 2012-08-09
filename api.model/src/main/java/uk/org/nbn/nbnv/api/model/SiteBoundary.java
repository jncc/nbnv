package uk.org.nbn.nbnv.api.model;

public class SiteBoundary {
    
    private int featureID;
    private String name;
    private String providerKey;
    private String siteBoundaryDatasetKey;
    private int siteBoundaryCategoryId;
    
    public SiteBoundary(){}
    
    public SiteBoundary(int featureID, String name, String providerKey, String siteBoundaryDatasetKey, int siteBoundaryCategoryId){
        super();
        this.featureID = featureID;
        this.name = name;
        this.providerKey = providerKey;
        this.siteBoundaryDatasetKey = siteBoundaryDatasetKey;
        this.siteBoundaryCategoryId = siteBoundaryCategoryId;
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
    
}