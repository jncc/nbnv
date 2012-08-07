package uk.org.nbn.nbnv.api.model;

import java.util.Date;

public class SiteBoundaryDataset {
    
    private String datasetkey;
    private String name;
    private String description;
    private Date dateUploaded;
    private String provider;
    private int providerID;
    private String siteBoundaryCategoryName;
    private int siteBoundaryCategoryID;
    private String geoLayerName;
    private String nameField;
    
    public SiteBoundaryDataset(){}

    public SiteBoundaryDataset(String datasetKey, String name, String description, Date dateUploaded, String provider, int providerID, String siteBoundaryCategoryName, int siteBoundaryCategoryID, String geoLayerName, String nameField){
        super();
        this.datasetkey = datasetKey;
        this.name = name;
        this.description = description;
        this.dateUploaded = dateUploaded;
        this.provider = provider;
        this.providerID = providerID;
        this.siteBoundaryCategoryName = siteBoundaryCategoryName;
        this.siteBoundaryCategoryID = siteBoundaryCategoryID;
        this.geoLayerName = geoLayerName;
        this.nameField = nameField;
    }

    public String getDatasetkey() {
        return datasetkey;
    }

    public void setDatasetkey(String datasetkey) {
        this.datasetkey = datasetkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    public String getSiteBoundaryCategoryName() {
        return siteBoundaryCategoryName;
    }

    public void setSiteBoundaryCategoryName(String siteBoundaryCategoryName) {
        this.siteBoundaryCategoryName = siteBoundaryCategoryName;
    }

    public int getSiteBoundaryCategoryID() {
        return siteBoundaryCategoryID;
    }

    public void setSiteBoundaryCategoryID(int siteBoundaryCategoryID) {
        this.siteBoundaryCategoryID = siteBoundaryCategoryID;
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

}
