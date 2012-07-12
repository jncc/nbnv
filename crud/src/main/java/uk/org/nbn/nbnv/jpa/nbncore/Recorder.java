/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Recorder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recorder.findAll", query = "SELECT r FROM Recorder r"),
    @NamedQuery(name = "Recorder.findByRecorderID", query = "SELECT r FROM Recorder r WHERE r.recorderID = :recorderID"),
    @NamedQuery(name = "Recorder.findByRecorderName", query = "SELECT r FROM Recorder r WHERE r.recorderName = :recorderName")})
public class Recorder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "recorderID")
    private Integer recorderID;
    @Basic(optional = false)
    @Column(name = "recorderName")
    private String recorderName;
    @OneToMany(mappedBy = "determinerID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(mappedBy = "recorderID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection1;
    @OneToMany(mappedBy = "determinerID")
    private Collection<TaxonObservation> taxonObservationCollection;
    @OneToMany(mappedBy = "recorderID")
    private Collection<TaxonObservation> taxonObservationCollection1;

    public Recorder() {
    }

    public Recorder(Integer recorderID) {
        this.recorderID = recorderID;
    }

    public Recorder(Integer recorderID, String recorderName) {
        this.recorderID = recorderID;
        this.recorderName = recorderName;
    }

    public Integer getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(Integer recorderID) {
        this.recorderID = recorderID;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
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
        hash += (recorderID != null ? recorderID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recorder)) {
            return false;
        }
        Recorder other = (Recorder) object;
        if ((this.recorderID == null && other.recorderID != null) || (this.recorderID != null && !this.recorderID.equals(other.recorderID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Recorder[ recorderID=" + recorderID + " ]";
    }
    
}
