package uk.org.nbn.nbnv.api.model;

import java.util.Date;

public class HabitatDataset {
    
    private String datasetKey;
    private String title;
    private String description;
    private Date dateUploaded;
    private String organisationName;
    private int organisationID;
    private int habitatCategory;
    private String habitatCategoryName;
    
    public HabitatDataset(){}

    public HabitatDataset(String datasetKey, String title, String description, Date dateUploaded, String organisationName, int organisationID, String habitatCategoryName, int habitatCategory){
        super();
        this.datasetKey = datasetKey;
        this.title = title;
        this.description = description;
        this.dateUploaded = dateUploaded;
        this.organisationName = organisationName;
        this.organisationID = organisationID;
        this.habitatCategory = habitatCategory;
        this.habitatCategoryName = habitatCategoryName;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public int getHabitatCategory() {
        return habitatCategory;
    }

    public void setHabitatCategory(int habitatCategory) {
        this.habitatCategory = habitatCategory;
    }

    public String getHabitatCategoryName() {
        return habitatCategoryName;
    }

    public void setHabitatCategoryName(String habitatCategoryName) {
        this.habitatCategoryName = habitatCategoryName;
    }

}
