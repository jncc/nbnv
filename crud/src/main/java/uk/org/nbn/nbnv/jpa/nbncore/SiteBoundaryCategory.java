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
@Table(name = "SiteBoundaryCategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundaryCategory.findAll", query = "SELECT s FROM SiteBoundaryCategory s"),
    @NamedQuery(name = "SiteBoundaryCategory.findById", query = "SELECT s FROM SiteBoundaryCategory s WHERE s.id = :id"),
    @NamedQuery(name = "SiteBoundaryCategory.findByName", query = "SELECT s FROM SiteBoundaryCategory s WHERE s.name = :name")})
public class SiteBoundaryCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundaryCategory")
    private Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection;

    public SiteBoundaryCategory() {
    }

    public SiteBoundaryCategory(Integer id) {
        this.id = id;
    }

    public SiteBoundaryCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<SiteBoundaryDataset> getSiteBoundaryDatasetCollection() {
        return siteBoundaryDatasetCollection;
    }

    public void setSiteBoundaryDatasetCollection(Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection) {
        this.siteBoundaryDatasetCollection = siteBoundaryDatasetCollection;
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
        if (!(object instanceof SiteBoundaryCategory)) {
            return false;
        }
        SiteBoundaryCategory other = (SiteBoundaryCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryCategory[ id=" + id + " ]";
    }
    
}
