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
@Table(name = "Survey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Survey.findAll", query = "SELECT s FROM Survey s"),
    @NamedQuery(name = "Survey.findById", query = "SELECT s FROM Survey s WHERE s.id = :id"),
    @NamedQuery(name = "Survey.findByProviderKey", query = "SELECT s FROM Survey s WHERE s.providerKey = :providerKey"),
    @NamedQuery(name = "Survey.findByTitle", query = "SELECT s FROM Survey s WHERE s.title = :title"),
    @NamedQuery(name = "Survey.findByDescription", query = "SELECT s FROM Survey s WHERE s.description = :description"),
    @NamedQuery(name = "Survey.findByGeographicalCoverage", query = "SELECT s FROM Survey s WHERE s.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Survey.findByTemporalCoverage", query = "SELECT s FROM Survey s WHERE s.temporalCoverage = :temporalCoverage")})
public class Survey implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    private Collection<Sample> sampleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    private Collection<SurveyAttribute> surveyAttributeCollection;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private TaxonDataset taxonDataset;

    public Survey() {
    }

    public Survey(Integer id) {
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
    public Collection<Sample> getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(Collection<Sample> sampleCollection) {
        this.sampleCollection = sampleCollection;
    }

    @XmlTransient
    public Collection<SurveyAttribute> getSurveyAttributeCollection() {
        return surveyAttributeCollection;
    }

    public void setSurveyAttributeCollection(Collection<SurveyAttribute> surveyAttributeCollection) {
        this.surveyAttributeCollection = surveyAttributeCollection;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Survey)) {
            return false;
        }
        Survey other = (Survey) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Survey[ id=" + id + " ]";
    }
    
}
