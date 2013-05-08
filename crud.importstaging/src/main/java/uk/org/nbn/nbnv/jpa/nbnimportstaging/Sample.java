/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author felix mason
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
    @JoinColumn(name = "surveyID", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Survey surveyID;

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

    public Survey getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(Survey surveyID) {
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
        return "uk.org.nbn.nbnv.jpa.nbnimportstaging.Sample[ id=" + id + " ]";
    }
    
}
