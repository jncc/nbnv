/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "TaxonGroup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonGroup.findAll", query = "SELECT t FROM TaxonGroup t"),
    @NamedQuery(name = "TaxonGroup.findByTaxonGroupKey", query = "SELECT t FROM TaxonGroup t WHERE t.taxonGroupKey = :taxonGroupKey"),
    @NamedQuery(name = "TaxonGroup.findBySortOrder", query = "SELECT t FROM TaxonGroup t WHERE t.sortOrder = :sortOrder"),
    @NamedQuery(name = "TaxonGroup.findByOutputFlag", query = "SELECT t FROM TaxonGroup t WHERE t.outputFlag = :outputFlag"),
    @NamedQuery(name = "TaxonGroup.findByTaxonGroupName", query = "SELECT t FROM TaxonGroup t WHERE t.taxonGroupName = :taxonGroupName"),
    @NamedQuery(name = "TaxonGroup.findByDescriptor", query = "SELECT t FROM TaxonGroup t WHERE t.descriptor = :descriptor")})
public class TaxonGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "taxonGroupKey")
    private String taxonGroupKey;
    @Column(name = "sortOrder")
    private Integer sortOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "outputFlag")
    private boolean outputFlag;
    @Size(max = 50)
    @Column(name = "taxonGroupName")
    private String taxonGroupName;
    @Size(max = 65)
    @Column(name = "descriptor")
    private String descriptor;
    @OneToMany(mappedBy = "parent")
    private Collection<TaxonGroup> taxonGroupCollection;
    @JoinColumn(name = "parent", referencedColumnName = "taxonGroupKey")
    @ManyToOne
    private TaxonGroup parent;
    @OneToMany(mappedBy = "taxonNavigationGroupKey")
    private Collection<Taxon> taxonCollection;
    @OneToMany(mappedBy = "taxonOutputGroupKey")
    private Collection<Taxon> taxonCollection1;

    public TaxonGroup() {
    }

    public TaxonGroup(String taxonGroupKey) {
        this.taxonGroupKey = taxonGroupKey;
    }

    public TaxonGroup(String taxonGroupKey, boolean outputFlag) {
        this.taxonGroupKey = taxonGroupKey;
        this.outputFlag = outputFlag;
    }

    public String getTaxonGroupKey() {
        return taxonGroupKey;
    }

    public void setTaxonGroupKey(String taxonGroupKey) {
        this.taxonGroupKey = taxonGroupKey;
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

    public String getTaxonGroupName() {
        return taxonGroupName;
    }

    public void setTaxonGroupName(String taxonGroupName) {
        this.taxonGroupName = taxonGroupName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @XmlTransient
    public Collection<TaxonGroup> getTaxonGroupCollection() {
        return taxonGroupCollection;
    }

    public void setTaxonGroupCollection(Collection<TaxonGroup> taxonGroupCollection) {
        this.taxonGroupCollection = taxonGroupCollection;
    }

    public TaxonGroup getParent() {
        return parent;
    }

    public void setParent(TaxonGroup parent) {
        this.parent = parent;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonGroupKey != null ? taxonGroupKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonGroup)) {
            return false;
        }
        TaxonGroup other = (TaxonGroup) object;
        if ((this.taxonGroupKey == null && other.taxonGroupKey != null) || (this.taxonGroupKey != null && !this.taxonGroupKey.equals(other.taxonGroupKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonGroup[ taxonGroupKey=" + taxonGroupKey + " ]";
    }
    
}
