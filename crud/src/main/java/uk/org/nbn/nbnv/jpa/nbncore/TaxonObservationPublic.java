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
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "TaxonObservationPublic.findByTaxonObservationID", query = "SELECT t FROM TaxonObservationPublic t WHERE t.taxonObservationID = :taxonObservationID")})
public class TaxonObservationPublic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonObservationID")
    private Integer taxonObservationID;
    @JoinColumn(name = "taxonObservationID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TaxonObservation taxonObservation;
    @JoinColumn(name = "siteID", referencedColumnName = "id")
    @ManyToOne
    private Site site;
    @JoinColumn(name = "determinerID", referencedColumnName = "id")
    @ManyToOne
    private Recorder recorder;
    @JoinColumn(name = "recorderID", referencedColumnName = "id")
    @ManyToOne
    private Recorder recorder1;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Feature feature;

    public TaxonObservationPublic() {
    }

    public TaxonObservationPublic(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public Integer getTaxonObservationID() {
        return taxonObservationID;
    }

    public void setTaxonObservationID(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public TaxonObservation getTaxonObservation() {
        return taxonObservation;
    }

    public void setTaxonObservation(TaxonObservation taxonObservation) {
        this.taxonObservation = taxonObservation;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    // manually renamed Determiner and Recorder properties

    public Recorder getDeterminer() {
        return recorder;
    }

    public void setDeterminer(Recorder recorder) {
        this.recorder = recorder;
    }

    public Recorder getRecorder() {
        return recorder1;
    }

    public void setRecorder(Recorder recorder1) {
        this.recorder1 = recorder1;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
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
