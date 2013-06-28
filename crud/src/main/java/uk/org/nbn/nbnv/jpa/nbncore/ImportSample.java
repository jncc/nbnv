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
@Table(name = "ImportSample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportSample.findAll", query = "SELECT i FROM ImportSample i"),
    @NamedQuery(name = "ImportSample.findById", query = "SELECT i FROM ImportSample i WHERE i.id = :id"),
    @NamedQuery(name = "ImportSample.findByProviderKey", query = "SELECT i FROM ImportSample i WHERE i.providerKey = :providerKey"),
    @NamedQuery(name = "ImportSample.findByTitle", query = "SELECT i FROM ImportSample i WHERE i.title = :title"),
    @NamedQuery(name = "ImportSample.findByDescription", query = "SELECT i FROM ImportSample i WHERE i.description = :description"),
    @NamedQuery(name = "ImportSample.findByGeographicalCoverage", query = "SELECT i FROM ImportSample i WHERE i.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "ImportSample.findByTemporalCoverage", query = "SELECT i FROM ImportSample i WHERE i.temporalCoverage = :temporalCoverage")})
public class ImportSample implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleID")
    private Collection<ImportTaxonObservation> importTaxonObservationCollection;
    @JoinColumn(name = "surveyID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ImportSurvey surveyID;

    public ImportSample() {
    }

    public ImportSample(Integer id) {
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

    @XmlTransient
    public Collection<ImportTaxonObservation> getImportTaxonObservationCollection() {
        return importTaxonObservationCollection;
    }

    public void setImportTaxonObservationCollection(Collection<ImportTaxonObservation> importTaxonObservationCollection) {
        this.importTaxonObservationCollection = importTaxonObservationCollection;
    }

    public ImportSurvey getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(ImportSurvey surveyID) {
        this.surveyID = surveyID;
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
        if (!(object instanceof ImportSample)) {
            return false;
        }
        ImportSample other = (ImportSample) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportSample[ id=" + id + " ]";
    }
    
}
