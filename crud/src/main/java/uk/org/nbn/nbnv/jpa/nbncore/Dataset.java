/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.validation.constraints.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataset.findAll", query = "SELECT d FROM Dataset d"),
    @NamedQuery(name = "Dataset.findByDatasetKey", query = "SELECT d FROM Dataset d WHERE d.datasetKey = :datasetKey"),
    @NamedQuery(name = "Dataset.findByConditionsAccepted", query = "SELECT d FROM Dataset d WHERE d.conditionsAccepted = :conditionsAccepted"),
    @NamedQuery(name = "Dataset.findByDateUploaded", query = "SELECT d FROM Dataset d WHERE d.dateUploaded = :dateUploaded"),
    @NamedQuery(name = "Dataset.findByMetadataLastEdited", query = "SELECT d FROM Dataset d WHERE d.metadataLastEdited = :metadataLastEdited"),
    @NamedQuery(name = "Dataset.findByProviderKey", query = "SELECT d FROM Dataset d WHERE d.providerKey = :providerKey"),
    @NamedQuery(name = "Dataset.findByDatasetTitle", query = "SELECT d FROM Dataset d WHERE d.datasetTitle = :datasetTitle"),
    @NamedQuery(name = "Dataset.findByDataCaptureMethod", query = "SELECT d FROM Dataset d WHERE d.dataCaptureMethod = :dataCaptureMethod"),
    @NamedQuery(name = "Dataset.findByPurpose", query = "SELECT d FROM Dataset d WHERE d.purpose = :purpose"),
    @NamedQuery(name = "Dataset.findByGeographicalCoverage", query = "SELECT d FROM Dataset d WHERE d.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Dataset.findByTemporalCoverage", query = "SELECT d FROM Dataset d WHERE d.temporalCoverage = :temporalCoverage"),
    @NamedQuery(name = "Dataset.findByDataQuality", query = "SELECT d FROM Dataset d WHERE d.dataQuality = :dataQuality"),
    @NamedQuery(name = "Dataset.findByAdditionalInformation", query = "SELECT d FROM Dataset d WHERE d.additionalInformation = :additionalInformation"),
    @NamedQuery(name = "Dataset.findByAccessConstraints", query = "SELECT d FROM Dataset d WHERE d.accessConstraints = :accessConstraints"),
    @NamedQuery(name = "Dataset.findByUseConstraints", query = "SELECT d FROM Dataset d WHERE d.useConstraints = :useConstraints"),
    @NamedQuery(name = "Dataset.findByDescription", query = "SELECT d FROM Dataset d WHERE d.description = :description")})
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "datasetKey")
    @Size(min = 8, max = 8, message = "Dataset Key needs to be eight characters long")
    private String datasetKey;
    @Basic(optional = false)
    @Column(name = "conditionsAccepted")
    private boolean conditionsAccepted;
    @Basic(optional = false)
    @Column(name = "dateUploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUploaded;
    @Basic(optional = false)
    @Column(name = "metadataLastEdited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date metadataLastEdited;
    @Column(name = "providerKey")
    private String providerKey;
    @Basic(optional = false)
    @Column(name = "datasetTitle")
    private String datasetTitle;
    @Column(name = "dataCaptureMethod")
    private String dataCaptureMethod;
    @Column(name = "purpose")
    private String purpose;
    @Column(name = "geographicalCoverage")
    private String geographicalCoverage;
    @Column(name = "temporalCoverage")
    private String temporalCoverage;
    @Column(name = "dataQuality")
    private String dataQuality;
    @Column(name = "additionalInformation")
    private String additionalInformation;
    @Column(name = "accessConstraints")
    private String accessConstraints;
    @Column(name = "useConstraints")
    private String useConstraints;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "datasetProvider", referencedColumnName = "organisationID")
    @ManyToOne(optional = false)
    private Organisation datasetProvider;
    @JoinColumn(name = "updateFrequency", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private DatasetUpdateFrequency updateFrequency;
    @JoinColumn(name = "datasetTypeKey", referencedColumnName = "datasetTypeKey")
    @ManyToOne(optional = false)
    private DatasetType datasetTypeKey;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataset")
    private TaxonDataset taxonDataset;

    public Dataset() {
    }

    public Dataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public Dataset(String datasetKey, boolean conditionsAccepted, Date dateUploaded, Date metadataLastEdited, String datasetTitle) {
        this.datasetKey = datasetKey;
        this.conditionsAccepted = conditionsAccepted;
        this.dateUploaded = dateUploaded;
        this.metadataLastEdited = metadataLastEdited;
        this.datasetTitle = datasetTitle;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public boolean getConditionsAccepted() {
        return conditionsAccepted;
    }

    public void setConditionsAccepted(boolean conditionsAccepted) {
        this.conditionsAccepted = conditionsAccepted;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Date getMetadataLastEdited() {
        return metadataLastEdited;
    }

    public void setMetadataLastEdited(Date metadataLastEdited) {
        this.metadataLastEdited = metadataLastEdited;
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

    public String getDataCaptureMethod() {
        return dataCaptureMethod;
    }

    public void setDataCaptureMethod(String dataCaptureMethod) {
        this.dataCaptureMethod = dataCaptureMethod;
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

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organisation getDatasetProvider() {
        return datasetProvider;
    }

    public void setDatasetProvider(Organisation datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    public DatasetUpdateFrequency getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(DatasetUpdateFrequency updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public DatasetType getDatasetTypeKey() {
        return datasetTypeKey;
    }

    public void setDatasetTypeKey(DatasetType datasetTypeKey) {
        this.datasetTypeKey = datasetTypeKey;
    }

    public TaxonDataset getTaxonDataset() {
        return taxonDataset;
    }

    public void setTaxonDataset(TaxonDataset taxonDataset) {
        this.taxonDataset = taxonDataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataset)) {
            return false;
        }
        Dataset other = (Dataset) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Dataset[ datasetKey=" + datasetKey + " ]";
    }
    
}
