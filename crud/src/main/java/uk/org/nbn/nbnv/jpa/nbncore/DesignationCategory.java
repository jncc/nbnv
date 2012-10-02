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
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "DesignationCategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesignationCategory.findAll", query = "SELECT d FROM DesignationCategory d"),
    @NamedQuery(name = "DesignationCategory.findById", query = "SELECT d FROM DesignationCategory d WHERE d.id = :id"),
    @NamedQuery(name = "DesignationCategory.findByLabel", query = "SELECT d FROM DesignationCategory d WHERE d.label = :label"),
    @NamedQuery(name = "DesignationCategory.findByDescription", query = "SELECT d FROM DesignationCategory d WHERE d.description = :description"),
    @NamedQuery(name = "DesignationCategory.findBySortOrder", query = "SELECT d FROM DesignationCategory d WHERE d.sortOrder = :sortOrder")})
public class DesignationCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sortOrder")
    private int sortOrder;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "designationCategoryID")
    private Collection<Designation> designationCollection;

    public DesignationCategory() {
    }

    public DesignationCategory(Integer id) {
        this.id = id;
    }

    public DesignationCategory(Integer id, String label, int sortOrder) {
        this.id = id;
        this.label = label;
        this.sortOrder = sortOrder;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @XmlTransient
    public Collection<Designation> getDesignationCollection() {
        return designationCollection;
    }

    public void setDesignationCollection(Collection<Designation> designationCollection) {
        this.designationCollection = designationCollection;
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
        if (!(object instanceof DesignationCategory)) {
            return false;
        }
        DesignationCategory other = (DesignationCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DesignationCategory[ id=" + id + " ]";
    }
    
}
