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
@Table(name = "DateType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DateType.findAll", query = "SELECT d FROM DateType d"),
    @NamedQuery(name = "DateType.findByDateTypeKey", query = "SELECT d FROM DateType d WHERE d.dateTypeKey = :dateTypeKey"),
    @NamedQuery(name = "DateType.findByLabel", query = "SELECT d FROM DateType d WHERE d.label = :label")})
public class DateType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "dateTypeKey")
    private String dateTypeKey;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dateType")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dateType")
    private Collection<TaxonObservation> taxonObservationCollection;

    public DateType() {
    }

    public DateType(String dateTypeKey) {
        this.dateTypeKey = dateTypeKey;
    }

    public DateType(String dateTypeKey, String label) {
        this.dateTypeKey = dateTypeKey;
        this.label = label;
    }

    public String getDateTypeKey() {
        return dateTypeKey;
    }

    public void setDateTypeKey(String dateTypeKey) {
        this.dateTypeKey = dateTypeKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateTypeKey != null ? dateTypeKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DateType)) {
            return false;
        }
        DateType other = (DateType) object;
        if ((this.dateTypeKey == null && other.dateTypeKey != null) || (this.dateTypeKey != null && !this.dateTypeKey.equals(other.dateTypeKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DateType[ dateTypeKey=" + dateTypeKey + " ]";
    }
    
}
