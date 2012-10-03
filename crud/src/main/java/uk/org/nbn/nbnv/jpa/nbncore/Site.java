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
@Table(name = "Site")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s"),
    @NamedQuery(name = "Site.findById", query = "SELECT s FROM Site s WHERE s.id = :id"),
    @NamedQuery(name = "Site.findByName", query = "SELECT s FROM Site s WHERE s.name = :name"),
    @NamedQuery(name = "Site.findByProviderKey", query = "SELECT s FROM Site s WHERE s.providerKey = :providerKey")})
public class Site implements Serializable {
    @OneToMany(mappedBy = "siteID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @OneToMany(mappedBy = "siteID")
    private Collection<TaxonObservation> taxonObservationCollection;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private Dataset datasetKey;

    public Site() {
    }

    public Site(Integer id) {
        this.id = id;
    }

    public Site(Integer id, String name) {
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

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    public Dataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(Dataset datasetKey) {
        this.datasetKey = datasetKey;
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
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Site[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }
    
}
