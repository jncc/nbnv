/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author felix mason
 */
@Entity
@Table(name = "Dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataset.findAll", query = "SELECT d FROM Dataset d"),
    @NamedQuery(name = "Dataset.findByKey", query = "SELECT d FROM Dataset d WHERE d.key = :key"),
    @NamedQuery(name = "Dataset.findByDatasetTypeKey", query = "SELECT d FROM Dataset d WHERE d.datasetTypeKey = :datasetTypeKey"),
    @NamedQuery(name = "Dataset.findByProviderOrganisationKey", query = "SELECT d FROM Dataset d WHERE d.providerOrganisationKey = :providerOrganisationKey"),
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
    @NamedQuery(name = "Dataset.findByUpdateFrequencyCode", query = "SELECT d FROM Dataset d WHERE d.updateFrequencyCode = :updateFrequencyCode"),
    @NamedQuery(name = "Dataset.findByDateUploaded", query = "SELECT d FROM Dataset d WHERE d.dateUploaded = :dateUploaded"),
    @NamedQuery(name = "Dataset.findByMetadataLastEdited", query = "SELECT d FROM Dataset d WHERE d.metadataLastEdited = :metadataLastEdited")})
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "key", nullable = false, length = 8)
    private String key;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datasetTypeKey", nullable = false)
    private char datasetTypeKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "providerOrganisationKey", nullable = false)
    private int providerOrganisationKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Size(max = 2147483647)
    @Column(name = "description", length = 2147483647)
    private String description;
    @Size(max = 500)
    @Column(name = "providerKey", length = 500)
    private String providerKey;
    @Size(max = 2147483647)
    @Column(name = "dataCaptureMethod", length = 2147483647)
    private String dataCaptureMethod;
    @Size(max = 2147483647)
    @Column(name = "purpose", length = 2147483647)
    private String purpose;
    @Size(max = 2147483647)
    @Column(name = "geographicalCoverage", length = 2147483647)
    private String geographicalCoverage;
    @Size(max = 2147483647)
    @Column(name = "temporalCoverage", length = 2147483647)
    private String temporalCoverage;
    @Size(max = 2147483647)
    @Column(name = "dataQuality", length = 2147483647)
    private String dataQuality;
    @Size(max = 2147483647)
    @Column(name = "additionalInformation", length = 2147483647)
    private String additionalInformation;
    @Size(max = 2147483647)
    @Column(name = "accessConstraints", length = 2147483647)
    private String accessConstraints;
    @Size(max = 2147483647)
    @Column(name = "useConstraints", length = 2147483647)
    private String useConstraints;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "updateFrequencyCode", nullable = false, length = 3)
    private String updateFrequencyCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateUploaded", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUploaded;
    @Basic(optional = false)
    @NotNull
    @Column(name = "metadataLastEdited", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date metadataLastEdited;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetKey")
    private Collection<Site> siteCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataset")
    private TaxonDataset taxonDataset;

    public Dataset() {
    }

    public Dataset(String key) {
        this.key = key;
    }

    public Dataset(String key, char datasetTypeKey, int providerOrganisationKey, String title, String updateFrequencyCode, Date dateUploaded, Date metadataLastEdited) {
        this.key = key;
        this.datasetTypeKey = datasetTypeKey;
        this.providerOrganisationKey = providerOrganisationKey;
        this.title = title;
        this.updateFrequencyCode = updateFrequencyCode;
        this.dateUploaded = dateUploaded;
        this.metadataLastEdited = metadataLastEdited;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public char getDatasetTypeKey() {
        return datasetTypeKey;
    }

    public void setDatasetTypeKey(char datasetTypeKey) {
        this.datasetTypeKey = datasetTypeKey;
    }

    public int getProviderOrganisationKey() {
        return providerOrganisationKey;
    }

    public void setProviderOrganisationKey(int providerOrganisationKey) {
        this.providerOrganisationKey = providerOrganisationKey;
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

    public String getUpdateFrequencyCode() {
        return updateFrequencyCode;
    }

    public void setUpdateFrequencyCode(String updateFrequencyCode) {
        this.updateFrequencyCode = updateFrequencyCode;
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
    public Collection<Site> getSiteCollection() {
        return siteCollection;
    }

    public void setSiteCollection(Collection<Site> siteCollection) {
        this.siteCollection = siteCollection;
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
        return "uk.org.nbn.nbnv.jpa.nbnimportstaging.Dataset[ key=" + key + " ]";
    }
    
}
