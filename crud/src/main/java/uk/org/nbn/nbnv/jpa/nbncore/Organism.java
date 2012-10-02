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
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Organism")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organism.findAll", query = "SELECT o FROM Organism o"),
    @NamedQuery(name = "Organism.findByKey", query = "SELECT o FROM Organism o WHERE o.key = :key")})
public class Organism implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "\"key\"")
    private String key;
    @OneToMany(mappedBy = "parentOrganismKey")
    private Collection<Organism> organismCollection;
    @JoinColumn(name = "parentOrganismKey", referencedColumnName = "key")
    @ManyToOne
    private Organism parentOrganismKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organismKey")
    private Collection<Taxon> taxonCollection;

    public Organism() {
    }

    public Organism(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlTransient
    public Collection<Organism> getOrganismCollection() {
        return organismCollection;
    }

    public void setOrganismCollection(Collection<Organism> organismCollection) {
        this.organismCollection = organismCollection;
    }

    public Organism getParentOrganismKey() {
        return parentOrganismKey;
    }

    public void setParentOrganismKey(Organism parentOrganismKey) {
        this.parentOrganismKey = parentOrganismKey;
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
        if (!(object instanceof Organism)) {
            return false;
        }
        Organism other = (Organism) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Organism[ key=" + key + " ]";
    }
    
}
