/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 100)
    @Column(name = "providerKey", length = 100)
    private String providerKey;
    @Size(max = 200)
    @Column(name = "title", length = 200)
    private String title;
    @Size(max = 2147483647)
    @Column(name = "description", length = 2147483647)
    private String description;
    @Size(max = 2147483647)
    @Column(name = "geographicalCoverage", length = 2147483647)
    private String geographicalCoverage;
    @Size(max = 2147483647)
    @Column(name = "temporalCoverage", length = 2147483647)
    private String temporalCoverage;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey", nullable = false)
    @ManyToOne(optional = false)
    private TaxonDataset datasetKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyID")
    private Collection<Sample> sampleCollection;

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

    public TaxonDataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(TaxonDataset datasetKey) {
        this.datasetKey = datasetKey;
    }

    @XmlTransient
    public Collection<Sample> getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(Collection<Sample> sampleCollection) {
        this.sampleCollection = sampleCollection;
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
        return "uk.org.nbn.nbnv.jpa.nbnimportstaging.Survey[ id=" + id + " ]";
    }
    
}
