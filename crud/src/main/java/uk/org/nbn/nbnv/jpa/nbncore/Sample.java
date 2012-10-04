/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sample.findAll", query = "SELECT s FROM Sample s"),
    @NamedQuery(name = "Sample.findById", query = "SELECT s FROM Sample s WHERE s.id = :id"),
    @NamedQuery(name = "Sample.findByProviderKey", query = "SELECT s FROM Sample s WHERE s.providerKey = :providerKey"),
    @NamedQuery(name = "Sample.findByTitle", query = "SELECT s FROM Sample s WHERE s.title = :title"),
    @NamedQuery(name = "Sample.findByDescription", query = "SELECT s FROM Sample s WHERE s.description = :description"),
    @NamedQuery(name = "Sample.findByGeographicalCoverage", query = "SELECT s FROM Sample s WHERE s.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Sample.findByTemporalCoverage", query = "SELECT s FROM Sample s WHERE s.temporalCoverage = :temporalCoverage")})
public class Sample implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    private Collection<TaxonObservation> taxonObservationCollection;
    @JoinColumn(name = "surveyID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Survey survey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    private Collection<SampleAttribute> sampleAttributeCollection;

    public Sample() {
    }

    public Sample(Integer id) {
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
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @XmlTransient
    public Collection<SampleAttribute> getSampleAttributeCollection() {
        return sampleAttributeCollection;
    }

    public void setSampleAttributeCollection(Collection<SampleAttribute> sampleAttributeCollection) {
        this.sampleAttributeCollection = sampleAttributeCollection;
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
        if (!(object instanceof Sample)) {
            return false;
        }
        Sample other = (Sample) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Sample[ id=" + id + " ]";
    }
    
}
