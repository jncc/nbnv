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
@Table(name = "TaxonNameStatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonNameStatus.findAll", query = "SELECT t FROM TaxonNameStatus t"),
    @NamedQuery(name = "TaxonNameStatus.findByKey", query = "SELECT t FROM TaxonNameStatus t WHERE t.key = :key"),
    @NamedQuery(name = "TaxonNameStatus.findByDescription", query = "SELECT t FROM TaxonNameStatus t WHERE t.description = :description")})
public class TaxonNameStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "\"key\"")
    private Character key;
    @Size(max = 50)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonNameStatus")
    private Collection<Taxon> taxonCollection;

    public TaxonNameStatus() {
    }

    public TaxonNameStatus(Character key) {
        this.key = key;
    }

    public Character getKey() {
        return key;
    }

    public void setKey(Character key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection() {
        return taxonCollection;
    }

    public void setTaxonCollection(Collection<Taxon> taxonCollection) {
        this.taxonCollection = taxonCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonNameStatus)) {
            return false;
        }
        TaxonNameStatus other = (TaxonNameStatus) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonNameStatus[ key=" + key + " ]";
    }
    
}
