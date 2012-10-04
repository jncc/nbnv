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
@Table(name = "TaxonRank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonRank.findAll", query = "SELECT t FROM TaxonRank t"),
    @NamedQuery(name = "TaxonRank.findById", query = "SELECT t FROM TaxonRank t WHERE t.id = :id"),
    @NamedQuery(name = "TaxonRank.findByRank", query = "SELECT t FROM TaxonRank t WHERE t.rank = :rank"),
    @NamedQuery(name = "TaxonRank.findByLevel", query = "SELECT t FROM TaxonRank t WHERE t.level = :level")})
public class TaxonRank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "rank")
    private String rank;
    @Basic(optional = false)
    @NotNull
    @Column(name = "level")
    private int level;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonRank")
    private Collection<Taxon> taxonCollection;

    public TaxonRank() {
    }

    public TaxonRank(Integer id) {
        this.id = id;
    }

    public TaxonRank(Integer id, String rank, int level) {
        this.id = id;
        this.rank = rank;
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonRank)) {
            return false;
        }
        TaxonRank other = (TaxonRank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonRank[ id=" + id + " ]";
    }
    
}
