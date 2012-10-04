/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Recorder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recorder.findAll", query = "SELECT r FROM Recorder r"),
    @NamedQuery(name = "Recorder.findById", query = "SELECT r FROM Recorder r WHERE r.id = :id"),
    @NamedQuery(name = "Recorder.findByName", query = "SELECT r FROM Recorder r WHERE r.name = :name")})
public class Recorder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "recorder")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(mappedBy = "recorder1")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection1;
    @OneToMany(mappedBy = "recorder")
    private Collection<TaxonObservation> taxonObservationCollection;
    @OneToMany(mappedBy = "recorder1")
    private Collection<TaxonObservation> taxonObservationCollection1;

    public Recorder() {
    }

    public Recorder(Integer id) {
        this.id = id;
    }

    public Recorder(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection1() {
        return taxonObservationPublicCollection1;
    }

    public void setTaxonObservationPublicCollection1(Collection<TaxonObservationPublic> taxonObservationPublicCollection1) {
        this.taxonObservationPublicCollection1 = taxonObservationPublicCollection1;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection1() {
        return taxonObservationCollection1;
    }

    public void setTaxonObservationCollection1(Collection<TaxonObservation> taxonObservationCollection1) {
        this.taxonObservationCollection1 = taxonObservationCollection1;
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
        if (!(object instanceof Recorder)) {
            return false;
        }
        Recorder other = (Recorder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Recorder[ id=" + id + " ]";
    }
    
}
