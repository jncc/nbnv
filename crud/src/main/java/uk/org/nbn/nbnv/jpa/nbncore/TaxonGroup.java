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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "TaxonGroup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonGroup.findAll", query = "SELECT t FROM TaxonGroup t"),
    @NamedQuery(name = "TaxonGroup.findByKey", query = "SELECT t FROM TaxonGroup t WHERE t.key = :key"),
    @NamedQuery(name = "TaxonGroup.findByName", query = "SELECT t FROM TaxonGroup t WHERE t.name = :name"),
    @NamedQuery(name = "TaxonGroup.findByDescription", query = "SELECT t FROM TaxonGroup t WHERE t.description = :description"),
    @NamedQuery(name = "TaxonGroup.findBySortOrder", query = "SELECT t FROM TaxonGroup t WHERE t.sortOrder = :sortOrder"),
    @NamedQuery(name = "TaxonGroup.findByOutputFlag", query = "SELECT t FROM TaxonGroup t WHERE t.outputFlag = :outputFlag")})
public class TaxonGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "\"key\"")
    private String key;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 65)
    @Column(name = "description")
    private String description;
    @Column(name = "sortOrder")
    private Integer sortOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "outputFlag")
    private boolean outputFlag;
    @ManyToMany(mappedBy = "taxonGroupCollection")
    private Collection<Taxon> taxonCollection;
    @OneToMany(mappedBy = "taxonOutputGroupKey")
    private Collection<Taxon> taxonCollection1;
    @OneToMany(mappedBy = "parentTaxonGroupKey")
    private Collection<TaxonGroup> taxonGroupCollection;
    @JoinColumn(name = "parentTaxonGroupKey", referencedColumnName = "key")
    @ManyToOne
    private TaxonGroup parentTaxonGroupKey;

    public TaxonGroup() {
    }

    public TaxonGroup(String key) {
        this.key = key;
    }

    public TaxonGroup(String key, boolean outputFlag) {
        this.key = key;
        this.outputFlag = outputFlag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean getOutputFlag() {
        return outputFlag;
    }

    public void setOutputFlag(boolean outputFlag) {
        this.outputFlag = outputFlag;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection() {
        return taxonCollection;
    }

    public void setTaxonCollection(Collection<Taxon> taxonCollection) {
        this.taxonCollection = taxonCollection;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection1() {
        return taxonCollection1;
    }

    public void setTaxonCollection1(Collection<Taxon> taxonCollection1) {
        this.taxonCollection1 = taxonCollection1;
    }

    @XmlTransient
    public Collection<TaxonGroup> getTaxonGroupCollection() {
        return taxonGroupCollection;
    }

    public void setTaxonGroupCollection(Collection<TaxonGroup> taxonGroupCollection) {
        this.taxonGroupCollection = taxonGroupCollection;
    }

    public TaxonGroup getParentTaxonGroupKey() {
        return parentTaxonGroupKey;
    }

    public void setParentTaxonGroupKey(TaxonGroup parentTaxonGroupKey) {
        this.parentTaxonGroupKey = parentTaxonGroupKey;
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
        if (!(object instanceof TaxonGroup)) {
            return false;
        }
        TaxonGroup other = (TaxonGroup) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonGroup[ key=" + key + " ]";
    }
    
}
