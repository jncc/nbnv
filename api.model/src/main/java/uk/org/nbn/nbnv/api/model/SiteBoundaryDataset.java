package uk.org.nbn.nbnv.api.model;

public class SiteBoundaryDataset {
    
    private String datasetkey;
    private String datasetTitle;
    private String geoLayerName;
    private String nameField;
    private int gisLayerID;
    private int siteBoundaryCategory;
    
    public SiteBoundaryDataset(){}

    public SiteBoundaryDataset(String datasetKey, String datasetTitle, String geoLayerName, String nameField, int gisLayerID, int siteBoundaryCategory){
        super();
        this.datasetkey = datasetKey;
        this.datasetTitle = datasetTitle;
        this.geoLayerName = geoLayerName;
        this.nameField = nameField;
        this.gisLayerID = gisLayerID;
        this.siteBoundaryCategory = siteBoundaryCategory;
    }

    public String getDatasetkey() {
        return datasetkey;
    }

    public void setDatasetkey(String datasetkey) {
        this.datasetkey = datasetkey;
    }

    public String getGeoLayerName() {
        return geoLayerName;
    }

    public void setGeoLayerName(String geoLayerName) {
        this.geoLayerName = geoLayerName;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public int getGisLayerID() {
        return gisLayerID;
    }

    public void setGisLayerID(int gisLayerID) {
        this.gisLayerID = gisLayerID;
    }

    public int getSiteBoundaryCategory() {
        return siteBoundaryCategory;
    }

    public void setSiteBoundaryCategory(int siteBoundaryCategory) {
        this.siteBoundaryCategory = siteBoundaryCategory;
    }

    public String getDatasetTitle() {
        return datasetTitle;
    }

    public void setDatasetTitle(String datasetTitle) {
        this.datasetTitle = datasetTitle;
    }
    
}
