/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "TaxonObservationFilterElementType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationFilterElementType.findAll", query = "SELECT t FROM TaxonObservationFilterElementType t"),
    @NamedQuery(name = "TaxonObservationFilterElementType.findById", query = "SELECT t FROM TaxonObservationFilterElementType t WHERE t.id = :id"),
    @NamedQuery(name = "TaxonObservationFilterElementType.findByLabel", query = "SELECT t FROM TaxonObservationFilterElementType t WHERE t.label = :label")})
public class TaxonObservationFilterElementType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterElementTypeID")
    private Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection;

    public TaxonObservationFilterElementType() {
    }

    public TaxonObservationFilterElementType(Integer id) {
        this.id = id;
    }

    public TaxonObservationFilterElementType(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<TaxonObservationFilterElement> getTaxonObservationFilterElementCollection() {
        return taxonObservationFilterElementCollection;
    }

    public void setTaxonObservationFilterElementCollection(Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection) {
        this.taxonObservationFilterElementCollection = taxonObservationFilterElementCollection;
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
        if (!(object instanceof TaxonObservationFilterElementType)) {
            return false;
        }
        TaxonObservationFilterElementType other = (TaxonObservationFilterElementType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationFilterElementType[ id=" + id + " ]";
    }
    
}
