/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author felix mason
 */
@Entity
@Table(name = "ImportSurvey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportSurvey.findAll", query = "SELECT i FROM ImportSurvey i"),
    @NamedQuery(name = "ImportSurvey.findById", query = "SELECT i FROM ImportSurvey i WHERE i.id = :id"),
    @NamedQuery(name = "ImportSurvey.findByProviderKey", query = "SELECT i FROM ImportSurvey i WHERE i.providerKey = :providerKey"),
    @NamedQuery(name = "ImportSurvey.findByTitle", query = "SELECT i FROM ImportSurvey i WHERE i.title = :title"),
    @NamedQuery(name = "ImportSurvey.findByDescription", query = "SELECT i FROM ImportSurvey i WHERE i.description = :description"),
    @NamedQuery(name = "ImportSurvey.findByGeographicalCoverage", query = "SELECT i FROM ImportSurvey i WHERE i.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "ImportSurvey.findByTemporalCoverage", query = "SELECT i FROM ImportSurvey i WHERE i.temporalCoverage = :temporalCoverage"),
    @NamedQuery(name = "ImportSurvey.findByDataCaptureMethod", query = "SELECT i FROM ImportSurvey i WHERE i.dataCaptureMethod = :dataCaptureMethod"),
    @NamedQuery(name = "ImportSurvey.findByPurpose", query = "SELECT i FROM ImportSurvey i WHERE i.purpose = :purpose"),
    @NamedQuery(name = "ImportSurvey.findByDataQuality", query = "SELECT i FROM ImportSurvey i WHERE i.dataQuality = :dataQuality"),
    @NamedQuery(name = "ImportSurvey.findByAdditionalInformation", query = "SELECT i FROM ImportSurvey i WHERE i.additionalInformation = :additionalInformation")})
public class ImportSurvey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Size(max = 200)
    @Column(name = "title")
    private String title;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "geographicalCoverage")
    private String geographicalCoverage;
    @Size(max = 2147483647)
    @Column(name = "temporalCoverage")
    private String temporalCoverage;
    @Size(max = 2147483647)
    @Column(name = "dataCaptureMethod")
    private String dataCaptureMethod;
    @Size(max = 2147483647)
    @Column(name = "purpose")
    private String purpose;
    @Size(max = 2147483647)
    @Column(name = "dataQuality")
    private String dataQuality;
    @Size(max = 2147483647)
    @Column(name = "additionalInformation")
    private String additionalInformation;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private ImportTaxonDataset datasetKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyID")
    private Collection<ImportSample> importSampleCollection;

    public ImportSurvey() {
    }

    public ImportSurvey(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
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

    public ImportTaxonDataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(ImportTaxonDataset datasetKey) {
        this.datasetKey = datasetKey;
    }

    @XmlTransient
    public Collection<ImportSample> getImportSampleCollection() {
        return importSampleCollection;
    }

    public void setImportSampleCollection(Collection<ImportSample> importSampleCollection) {
        this.importSampleCollection = importSampleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportSurvey)) {
            return false;
        }
        ImportSurvey other = (ImportSurvey) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportSurvey[ id=" + id + " ]";
    }
    
}
