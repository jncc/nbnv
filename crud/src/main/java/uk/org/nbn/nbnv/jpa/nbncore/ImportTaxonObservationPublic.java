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
 * @author felix mason
 */
@Entity
@Table(name = "ImportTaxonObservationPublic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportTaxonObservationPublic.findAll", query = "SELECT i FROM ImportTaxonObservationPublic i"),
    @NamedQuery(name = "ImportTaxonObservationPublic.findByTaxonObservationID", query = "SELECT i FROM ImportTaxonObservationPublic i WHERE i.taxonObservationID = :taxonObservationID")})
public class ImportTaxonObservationPublic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonObservationID")
    private Integer taxonObservationID;
    @JoinColumn(name = "taxonObservationID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ImportTaxonObservation importTaxonObservation;
    @JoinColumn(name = "siteID", referencedColumnName = "id")
    @ManyToOne
    private ImportSite siteID;
    @JoinColumn(name = "recorderID", referencedColumnName = "id")
    @ManyToOne
    private ImportRecorder recorderID;
    @JoinColumn(name = "determinerID", referencedColumnName = "id")
    @ManyToOne
    private ImportRecorder determinerID;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ImportFeature featureID;

    public ImportTaxonObservationPublic() {
    }

    public ImportTaxonObservationPublic(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public Integer getTaxonObservationID() {
        return taxonObservationID;
    }

    public void setTaxonObservationID(Integer taxonObservationID) {
        this.taxonObservationID = taxonObservationID;
    }

    public ImportTaxonObservation getImportTaxonObservation() {
        return importTaxonObservation;
    }

    public void setImportTaxonObservation(ImportTaxonObservation importTaxonObservation) {
        this.importTaxonObservation = importTaxonObservation;
    }

    public ImportSite getSiteID() {
        return siteID;
    }

    public void setSiteID(ImportSite siteID) {
        this.siteID = siteID;
    }

    public ImportRecorder getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(ImportRecorder recorderID) {
        this.recorderID = recorderID;
    }

    public ImportRecorder getDeterminerID() {
        return determinerID;
    }

    public void setDeterminerID(ImportRecorder determinerID) {
        this.determinerID = determinerID;
    }

    public ImportFeature getFeatureID() {
        return featureID;
    }

    public void setFeatureID(ImportFeature featureID) {
        this.featureID = featureID;
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
        if (!(object instanceof ImportTaxonObservationPublic)) {
            return false;
        }
        ImportTaxonObservationPublic other = (ImportTaxonObservationPublic) object;
        if ((this.taxonObservationID == null && other.taxonObservationID != null) || (this.taxonObservationID != null && !this.taxonObservationID.equals(other.taxonObservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportTaxonObservationPublic[ taxonObservationID=" + taxonObservationID + " ]";
    }
    
}
