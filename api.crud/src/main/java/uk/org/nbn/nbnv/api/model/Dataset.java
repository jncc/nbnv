package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Dataset {
    @Ref(value="${resource.portalUrl}/Datasets/${instance.key}", style=Ref.Style.RELATIVE_PATH) 
    private URI href;
    
    @Ref(value="/organisations/${instance.organisationID}", style=Ref.Style.ABSOLUTE)
    private URI organisationHref;
    
    private String key, title, description, typeName, organisationName;
    private String captureMethod, purpose, geographicalCoverage, quality;
    private String additionalInformation, accessConstraints, useConstraints;
    private String temporalCoverage, updateFrequency;
    private Date dateUploaded, metadataLastEdited;
    private String formattedDateUploaded, formattedMetadataLastEdited;
    private int organisationID;
    private boolean conditionsAccepted;
    private Organisation organisation;
    private List<Organisation> contributingOrganisations;
    private Integer licenceID;
    private DatasetLicence datasetLicence;
    
    public Dataset(){}

    public Dataset(String key, String title, String description, String typeName, String organisationName,
                        String captureMethod, String purpose, String geographicalCoverage, String quality,
                        String additionalInformation, String accessConstraints, String useConstraints,
                        Date dateUploaded, int organisationID, boolean conditionsAccepted, Date metadataLastEdited,
                        String temporalCoverage, String updateFrequency, Organisation organisation){
        this.key = key;
        this.title = title;
        this.description = description;
        this.typeName = typeName;
        this.organisationName = organisationName;
        this.organisationID = organisationID;
        this.captureMethod = captureMethod;
        this.purpose = purpose;
        this.geographicalCoverage = geographicalCoverage;
        this.quality = quality;
        this.additionalInformation = additionalInformation;
        this.accessConstraints = accessConstraints;
        this.useConstraints = useConstraints;
        this.dateUploaded = dateUploaded;
        this.formattedDateUploaded = getFormattedDateUploaded();
        this.conditionsAccepted = conditionsAccepted;
        this.metadataLastEdited = metadataLastEdited;
        this.formattedMetadataLastEdited = getFormattedMetadataLastEdited();
        this.temporalCoverage = temporalCoverage;
        this.updateFrequency = updateFrequency;
        this.organisation = organisation;
    }

    public Dataset(String key, String title, String description, 
                        String captureMethod, String purpose, String geographicalCoverage, 
						String quality, String additionalInformation, String accessConstraints, 
						String useConstraints, String temporalCoverage, int licenceID) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.captureMethod = captureMethod;
        this.purpose = purpose;
        this.geographicalCoverage = geographicalCoverage;
        this.quality = quality;
        this.additionalInformation = additionalInformation;
        this.accessConstraints = accessConstraints;
        this.useConstraints = useConstraints;
        this.temporalCoverage = temporalCoverage;
		this.licenceID = licenceID;
    }

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getTypeName() {
        return typeName;
    }

    public void setType(String typeName) {
        this.setTypeName(typeName);
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getCaptureMethod() {
        return captureMethod;
    }

    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAccessConstraints() {
        return accessConstraints;
    }

    public void setAccessConstraints(String accessConstraints) {
        this.accessConstraints = accessConstraints;
    }

    public String getUseConstraints() {
        return useConstraints;
    }

    public void setUseConstraints(String useConstraints) {
        this.useConstraints = useConstraints;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }
    
    public String getFormattedDateUploaded(){
        return (new SimpleDateFormat("dd-MMM-yyyy")).format(dateUploaded);
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public String getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public Date getMetadataLastEdited() {
        return metadataLastEdited;
    }

    public String getFormattedMetadataLastEdited(){
        return (new SimpleDateFormat("dd-MMM-yyyy")).format(metadataLastEdited);
    }

    public void setMetadataLastEdited(Date metadataLastEdited) {
        this.metadataLastEdited = metadataLastEdited;
    }

    public boolean isConditionsAccepted() {
        return conditionsAccepted;
    }

    public void setConditionsAccepted(boolean conditionsAccepted) {
        this.conditionsAccepted = conditionsAccepted;
    }
    
    public Organisation getOrganisation(){
        return this.organisation;
    }
    
    public void setOrganisation(Organisation organisation){
        this.organisation = organisation;
    }

    public URI getOrganisationHref() {
        return organisationHref;
    }

    public void setOrganisationHref(URI organisationHref) {
        this.organisationHref = organisationHref;
    }

    /**
     * @return the contributingOrganisations
     */
    public List<Organisation> getContributingOrganisations() {
        return contributingOrganisations;
    }

    /**
     * @param contributingOrganisations the contributingOrganisations to set
     */
    public void setContributingOrganisations(List<Organisation> contributingOrganisations) {
        this.contributingOrganisations = contributingOrganisations;
    }

    public Integer getLicenceID() {
            return licenceID;
    }

    public void setLicenceID(Integer licenceID) {
            this.licenceID = licenceID;
    }

    public DatasetLicence getDatasetLicence() {
        return datasetLicence;
    }

    public void setDatasetLicence(DatasetLicence datasetLicence) {
        this.datasetLicence = datasetLicence;
    }    
}
