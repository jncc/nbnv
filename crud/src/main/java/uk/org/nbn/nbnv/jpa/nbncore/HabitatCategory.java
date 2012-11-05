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
@Table(name = "HabitatCategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HabitatCategory.findAll", query = "SELECT h FROM HabitatCategory h"),
    @NamedQuery(name = "HabitatCategory.findById", query = "SELECT h FROM HabitatCategory h WHERE h.id = :id"),
    @NamedQuery(name = "HabitatCategory.findByName", query = "SELECT h FROM HabitatCategory h WHERE h.name = :name")})
public class HabitatCategory implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "habitatCategory")
    private Collection<HabitatDataset> habitatDatasetCollection;

    public HabitatCategory() {
    }

    public HabitatCategory(Integer id) {
        this.id = id;
    }

    public HabitatCategory(Integer id, String name) {
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
    public Collection<HabitatDataset> getHabitatDatasetCollection() {
        return habitatDatasetCollection;
    }

    public void setHabitatDatasetCollection(Collection<HabitatDataset> habitatDatasetCollection) {
        this.habitatDatasetCollection = habitatDatasetCollection;
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
        if (!(object instanceof HabitatCategory)) {
            return false;
        }
        HabitatCategory other = (HabitatCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.HabitatCategory[ id=" + id + " ]";
    }
    
}
