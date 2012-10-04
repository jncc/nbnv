/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataset.findAll", query = "SELECT d FROM Dataset d"),
    @NamedQuery(name = "Dataset.findByKey", query = "SELECT d FROM Dataset d WHERE d.key = :key"),
    @NamedQuery(name = "Dataset.findByTitle", query = "SELECT d FROM Dataset d WHERE d.title = :title"),
    @NamedQuery(name = "Dataset.findByDescription", query = "SELECT d FROM Dataset d WHERE d.description = :description"),
    @NamedQuery(name = "Dataset.findByProviderKey", query = "SELECT d FROM Dataset d WHERE d.providerKey = :providerKey"),
    @NamedQuery(name = "Dataset.findByDataCaptureMethod", query = "SELECT d FROM Dataset d WHERE d.dataCaptureMethod = :dataCaptureMethod"),
    @NamedQuery(name = "Dataset.findByPurpose", query = "SELECT d FROM Dataset d WHERE d.purpose = :purpose"),
    @NamedQuery(name = "Dataset.findByGeographicalCoverage", query = "SELECT d FROM Dataset d WHERE d.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Dataset.findByTemporalCoverage", query = "SELECT d FROM Dataset d WHERE d.temporalCoverage = :temporalCoverage"),
    @NamedQuery(name = "Dataset.findByDataQuality", query = "SELECT d FROM Dataset d WHERE d.dataQuality = :dataQuality"),
    @NamedQuery(name = "Dataset.findByAdditionalInformation", query = "SELECT d FROM Dataset d WHERE d.additionalInformation = :additionalInformation"),
    @NamedQuery(name = "Dataset.findByAccessConstraints", query = "SELECT d FROM Dataset d WHERE d.accessConstraints = :accessConstraints"),
    @NamedQuery(name = "Dataset.findByUseConstraints", query = "SELECT d FROM Dataset d WHERE d.useConstraints = :useConstraints"),
    @NamedQuery(name = "Dataset.findByDateUploaded", query = "SELECT d FROM Dataset d WHERE d.dateUploaded = :dateUploaded"),
    @NamedQuery(name = "Dataset.findByMetadataLastEdited", query = "SELECT d FROM Dataset d WHERE d.metadataLastEdited = :metadataLastEdited")})
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "\"key\"")
    private String key;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "title")
    private String title;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 500)
    @Column(name = "providerKey")
    private String providerKey;
    @Size(max = 2147483647)
    @Column(name = "dataCaptureMethod")
    private String dataCaptureMethod;
    @Size(max = 2147483647)
    @Column(name = "purpose")
    private String purpose;
    @Size(max = 2147483647)
    @Column(name = "geographicalCoverage")
    private String geographicalCoverage;
    @Size(max = 2147483647)
    @Column(name = "temporalCoverage")
    private String temporalCoverage;
    @Size(max = 2147483647)
    @Column(name = "dataQuality")
    private String dataQuality;
    @Size(max = 2147483647)
    @Column(name = "additionalInformation")
    private String additionalInformation;
    @Size(max = 2147483647)
    @Column(name = "accessConstraints")
    private String accessConstraints;
    @Size(max = 2147483647)
    @Column(name = "useConstraints")
    private String useConstraints;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateUploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUploaded;
    @Basic(optional = false)
    @NotNull
    @Column(name = "metadataLastEdited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date metadataLastEdited;
    @JoinTable(name = "DatasetAdministrator", joinColumns = {
        @JoinColumn(name = "datasetKey", referencedColumnName = "key")}, inverseJoinColumns = {
        @JoinColumn(name = "userID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<User> userCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataset")
    private Collection<Site> siteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataset")
    private Collection<DatasetKeyword> datasetKeywordCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataset")
    private TaxonDataset taxonDataset;
    @JoinColumn(name = "providerOrganisationKey", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Organisation organisation;
    @JoinColumn(name = "updateFrequencyCode", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private DatasetUpdateFrequency datasetUpdateFrequency;
    @JoinColumn(name = "datasetTypeKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private DatasetType datasetType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataset")
    private Collection<DatasetAttribute> datasetAttributeCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataset")
    private HabitatDataset habitatDataset;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataset")
    private SiteBoundaryDataset siteBoundaryDataset;

    public Dataset() {
    }

    public Dataset(String key) {
        this.key = key;
    }

    public Dataset(String key, String title, Date dateUploaded, Date metadataLastEdited) {
        this.key = key;
        this.title = title;
        this.dateUploaded = dateUploaded;
        this.metadataLastEdited = metadataLastEdited;
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

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
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

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<Site> getSiteCollection() {
        return siteCollection;
    }

    public void setSiteCollection(Collection<Site> siteCollection) {
        this.siteCollection = siteCollection;
    }

    @XmlTransient
    public Collection<DatasetKeyword> getDatasetKeywordCollection() {
        return datasetKeywordCollection;
    }

    public void setDatasetKeywordCollection(Collection<DatasetKeyword> datasetKeywordCollection) {
        this.datasetKeywordCollection = datasetKeywordCollection;
    }

    public TaxonDataset getTaxonDataset() {
        return taxonDataset;
    }

    public void setTaxonDataset(TaxonDataset taxonDataset) {
        this.taxonDataset = taxonDataset;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public DatasetUpdateFrequency getDatasetUpdateFrequency() {
        return datasetUpdateFrequency;
    }

    public void setDatasetUpdateFrequency(DatasetUpdateFrequency datasetUpdateFrequency) {
        this.datasetUpdateFrequency = datasetUpdateFrequency;
    }

    public DatasetType getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(DatasetType datasetType) {
        this.datasetType = datasetType;
    }

    @XmlTransient
    public Collection<DatasetAttribute> getDatasetAttributeCollection() {
        return datasetAttributeCollection;
    }

    public void setDatasetAttributeCollection(Collection<DatasetAttribute> datasetAttributeCollection) {
        this.datasetAttributeCollection = datasetAttributeCollection;
    }

    public HabitatDataset getHabitatDataset() {
        return habitatDataset;
    }

    public void setHabitatDataset(HabitatDataset habitatDataset) {
        this.habitatDataset = habitatDataset;
    }

    public SiteBoundaryDataset getSiteBoundaryDataset() {
        return siteBoundaryDataset;
    }

    public void setSiteBoundaryDataset(SiteBoundaryDataset siteBoundaryDataset) {
        this.siteBoundaryDataset = siteBoundaryDataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataset)) {
            return false;
        }
        Dataset other = (Dataset) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Dataset[ key=" + key + " ]";
    }
    
}
