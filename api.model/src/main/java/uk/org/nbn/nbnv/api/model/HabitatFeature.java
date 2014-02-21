package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HabitatFeature {
    
    @Ref(value="${resource.portalUrl}/Datasets/${instance.habitatDatasetKey}", style=Ref.Style.RELATIVE_PATH) 
    private URI datasetHref;
    
    private String identifier, habitatDatasetKey, providerKey, datasetTitle, formattedUploadDate;
    private Date uploadDate;
    
    public HabitatFeature(){}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getHabitatDatasetKey() {
        return habitatDatasetKey;
    }

    public void setHabitatDatasetKey(String habitatDatasetKey) {
        this.habitatDatasetKey = habitatDatasetKey;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getDatasetTitle() {
        return datasetTitle;
    }

    public void setDatasetTitle(String datasetTitle) {
        this.datasetTitle = datasetTitle;
    }

    public String getFormattedUploadDate() {
        return (new SimpleDateFormat("dd-MMM-yyyy")).format(this.uploadDate);
    }

    public void setFormattedUploadDate(String formattedUploadDate) {
        this.formattedUploadDate = formattedUploadDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public URI getDatasetHref() {
        return this.datasetHref;
    }

    public void setDatasetHref(URI datasetHref) {
        this.datasetHref = datasetHref;
    }


}
