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
@Table(name = "ImportDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportDataset.findAll", query = "SELECT i FROM ImportDataset i"),
    @NamedQuery(name = "ImportDataset.findByKey", query = "SELECT i FROM ImportDataset i WHERE i.key = :key"),
    @NamedQuery(name = "ImportDataset.findByDatasetTypeKey", query = "SELECT i FROM ImportDataset i WHERE i.datasetTypeKey = :datasetTypeKey"),
    @NamedQuery(name = "ImportDataset.findByProviderOrganisationKey", query = "SELECT i FROM ImportDataset i WHERE i.providerOrganisationKey = :providerOrganisationKey"),
    @NamedQuery(name = "ImportDataset.findByTitle", query = "SELECT i FROM ImportDataset i WHERE i.title = :title"),
    @NamedQuery(name = "ImportDataset.findByDescription", query = "SELECT i FROM ImportDataset i WHERE i.description = :description"),
    @NamedQuery(name = "ImportDataset.findByProviderKey", query = "SELECT i FROM ImportDataset i WHERE i.providerKey = :providerKey"),
    @NamedQuery(name = "ImportDataset.findByDataCaptureMethod", query = "SELECT i FROM ImportDataset i WHERE i.dataCaptureMethod = :dataCaptureMethod"),
    @NamedQuery(name = "ImportDataset.findByPurpose", query = "SELECT i FROM ImportDataset i WHERE i.purpose = :purpose"),
    @NamedQuery(name = "ImportDataset.findByGeographicalCoverage", query = "SELECT i FROM ImportDataset i WHERE i.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "ImportDataset.findByTemporalCoverage", query = "SELECT i FROM ImportDataset i WHERE i.temporalCoverage = :temporalCoverage"),
    @NamedQuery(name = "ImportDataset.findByDataQuality", query = "SELECT i FROM ImportDataset i WHERE i.dataQuality = :dataQuality"),
    @NamedQuery(name = "ImportDataset.findByAdditionalInformation", query = "SELECT i FROM ImportDataset i WHERE i.additionalInformation = :additionalInformation"),
    @NamedQuery(name = "ImportDataset.findByAccessConstraints", query = "SELECT i FROM ImportDataset i WHERE i.accessConstraints = :accessConstraints"),
    @NamedQuery(name = "ImportDataset.findByUseConstraints", query = "SELECT i FROM ImportDataset i WHERE i.useConstraints = :useConstraints"),
    @NamedQuery(name = "ImportDataset.findByUpdateFrequencyCode", query = "SELECT i FROM ImportDataset i WHERE i.updateFrequencyCode = :updateFrequencyCode"),
    @NamedQuery(name = "ImportDataset.findByDateUploaded", query = "SELECT i FROM ImportDataset i WHERE i.dateUploaded = :dateUploaded"),
    @NamedQuery(name = "ImportDataset.findByMetadataLastEdited", query = "SELECT i FROM ImportDataset i WHERE i.metadataLastEdited = :metadataLastEdited")})
public class ImportDataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "\"key\"")
    private String key;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datasetTypeKey")
    private char datasetTypeKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "providerOrganisationKey")
    private int providerOrganisationKey;
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
    @Size(min = 1, max = 3)
    @Column(name = "updateFrequencyCode")
    private String updateFrequencyCode;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetKey")
    private Collection<ImportSite> importSiteCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "importDataset")
    private ImportTaxonDataset importTaxonDataset;
    @Size(max = 2147483647)
    @Column(name = "administratorForename")
    private String administratorForename;
    @Size(max = 2147483647)
    @Column(name = "administratorSurname")
    private String administratorSurname;
    @Size(max = 2147483647)
    @Column(name = "administratorEmail")
    private String administratorEmail;

    public ImportDataset() {
    }

    public ImportDataset(String key) {
        this.key = key;
    }

    public ImportDataset(String key, char datasetTypeKey, int providerOrganisationKey, String title, String updateFrequencyCode, Date dateUploaded, Date metadataLastEdited) {
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
    
    public String getAdministratorEmail() {
        return administratorEmail;
    }

    public void setAdministratorEmail(String administratorEmail) {
        this.administratorEmail = administratorEmail;
    }
    
    public String getAdministratorForename() {
        return administratorForename;
    }

    public void setAdministratorForename(String administratorForename) {
        this.administratorForename = administratorForename;
    }
    
    public String getAdministratorSurname() {
        return administratorSurname;
    }

    public void setAdministratorSurname(String administratorSurname) {
        this.administratorSurname = administratorSurname;
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
    public Collection<ImportSite> getImportSiteCollection() {
        return importSiteCollection;
    }

    public void setImportSiteCollection(Collection<ImportSite> importSiteCollection) {
        this.importSiteCollection = importSiteCollection;
    }

    public ImportTaxonDataset getImportTaxonDataset() {
        return importTaxonDataset;
    }

    public void setImportTaxonDataset(ImportTaxonDataset importTaxonDataset) {
        this.importTaxonDataset = importTaxonDataset;
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
        if (!(object instanceof ImportDataset)) {
            return false;
        }
        ImportDataset other = (ImportDataset) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportDataset[ key=" + key + " ]";
    }
    
}
