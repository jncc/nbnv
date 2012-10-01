/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonObservationPublic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationPublic.findAll", query = "SELECT t FROM TaxonObservationPublic t"),
    @NamedQuery(name = "TaxonObservationPublic.findByTaxonObservationID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.taxonObservationID = :taxonObservationID"),
    @NamedQuery(name = "TaxonObservationPublic.findBySiteID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.siteID = :siteID"),
    @NamedQuery(name = "TaxonObservationPublic.findByFeatureID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.featureID = :featureID"),
    @NamedQuery(name = "TaxonObservationPublic.findByRecorderID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.recorderID = :recorderID"),
    @NamedQuery(name = "TaxonObservationPublic.findByDeterminerID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.determinerID = :determinerID")})
public class TaxonObservationPublic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonObservationID")
    private Integer taxonObservationID;
    @Column(name = "siteID")
    private Integer siteID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "featureID")
    private int featureID;
    @Column(name = "recorderID")
    private Integer recorderID;
    @Column(name = "determinerID")
    private Integer determinerID;
    @JoinColumn(name = "taxonObservationID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TaxonObservation taxonObservation;

    public TaxonObservationPublic() {
    }

    public TaxonObservationPublic(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public TaxonObservationPublic(Integer taxonObservationID, int featureID) {
        this.taxonObservationID = taxonObservationID;
        this.featureID = featureID;
    }

    public Integer getTaxonObservationID() {
        return taxonObservationID;
    }

    public void setTaxonObservationID(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public Integer getSiteID() {
        return siteID;
    }

    public void setSiteID(Integer siteID) {
        this.siteID = siteID;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public Integer getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(Integer recorderID) {
        this.recorderID = recorderID;
    }

    public Integer getDeterminerID() {
        return determinerID;
    }

    public void setDeterminerID(Integer determinerID) {
        this.determinerID = determinerID;
    }

    public TaxonObservation getTaxonObservation() {
        return taxonObservation;
    }

    public void setTaxonObservation(TaxonObservation taxonObservation) {
        this.taxonObservation = taxonObservation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonObservationID != null ? taxonObservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservationPublic)) {
            return false;
        }
        TaxonObservationPublic other = (TaxonObservationPublic) object;
        if ((this.taxonObservationID == null && other.taxonObservationID != null) || (this.taxonObservationID != null && !this.taxonObservationID.equals(other.taxonObservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationPublic[ taxonObservationID=" + taxonObservationID + " ]";
    }
    
}
