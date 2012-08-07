package uk.org.nbn.nbnv.api.model;

import java.util.List;

public class SiteBoundaryCategory {
    
    private int siteBoundaryCategoryID;
    private String name;
    private List<SiteBoundaryDataset> siteBoundaryDatasets;
    
    public SiteBoundaryCategory(){}
    
    public SiteBoundaryCategory(int siteBoundaryCategoryID, String name, List<SiteBoundaryDataset> siteBoundaryDatasets){
        super();
        this.siteBoundaryCategoryID = siteBoundaryCategoryID;
        this.name = name;
        this.siteBoundaryDatasets = siteBoundaryDatasets;
    }

    public int getSiteBoundaryCategoryID() {
        return siteBoundaryCategoryID;
    }

    public void setSiteBoundaryCategoryID(int siteBoundaryCategoryID) {
        this.siteBoundaryCategoryID = siteBoundaryCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SiteBoundaryDataset> getSiteBoundaryDatasets() {
        return siteBoundaryDatasets;
    }

    public void setSiteBoundaryDatasets(List<SiteBoundaryDataset> siteBoundaryDatasets) {
        this.siteBoundaryDatasets = siteBoundaryDatasets;
    }
    
}
