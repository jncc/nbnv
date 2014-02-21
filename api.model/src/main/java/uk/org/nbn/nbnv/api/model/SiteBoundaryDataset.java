package uk.org.nbn.nbnv.api.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SiteBoundaryDataset {
    
    private String datasetKey;
    private String title;
    private String description;
    private Date dateUploaded;
    private String organisationName;
    private int organisationID;
    private String nameField;
    private int siteBoundaryCategory;
    private String siteBoundaryCategoryName;
    
    public SiteBoundaryDataset(){}

    public SiteBoundaryDataset(String datasetKey, String title, String description, Date dateUploaded, String organisationName, int organisationID, String siteBoundaryCategoryName, int siteBoundaryCategory, String nameField){
        super();
        this.datasetKey = datasetKey;
        this.title = title;
        this.description = description;
        this.dateUploaded = dateUploaded;
        this.organisationName = organisationName;
        this.organisationID = organisationID;
        this.siteBoundaryCategoryName = siteBoundaryCategoryName;
        this.siteBoundaryCategory = siteBoundaryCategory;
        this.nameField = nameField;
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

    public String getSiteBoundaryCategoryName() {
        return siteBoundaryCategoryName;
    }

    public void setSiteBoundaryCategoryName(String siteBoundaryCategoryName) {
        this.siteBoundaryCategoryName = siteBoundaryCategoryName;
    }

    public int getSiteBoundaryCategory() {
        return siteBoundaryCategory;
    }

    public void setSiteBoundaryCategory(int siteBoundaryCategory) {
        this.siteBoundaryCategory = siteBoundaryCategory;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

}
