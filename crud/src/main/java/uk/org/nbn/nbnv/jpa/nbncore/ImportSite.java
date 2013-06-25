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
 * @author felix mason
 */
@Entity
@Table(name = "ImportSite")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportSite.findAll", query = "SELECT i FROM ImportSite i"),
    @NamedQuery(name = "ImportSite.findById", query = "SELECT i FROM ImportSite i WHERE i.id = :id"),
    @NamedQuery(name = "ImportSite.findByName", query = "SELECT i FROM ImportSite i WHERE i.name = :name"),
    @NamedQuery(name = "ImportSite.findByProviderKey", query = "SELECT i FROM ImportSite i WHERE i.providerKey = :providerKey")})
public class ImportSite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    private Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection;
    @OneToMany(mappedBy = "siteID")
    private Collection<ImportTaxonObservation> importTaxonObservationCollection;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private ImportDataset datasetKey;

    public ImportSite() {
    }

    public ImportSite(Integer id) {
        this.id = id;
    }

    public ImportSite(Integer id, String name) {
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
    public Collection<ImportTaxonObservationPublic> getImportTaxonObservationPublicCollection() {
        return importTaxonObservationPublicCollection;
    }

    public void setImportTaxonObservationPublicCollection(Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection) {
        this.importTaxonObservationPublicCollection = importTaxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<ImportTaxonObservation> getImportTaxonObservationCollection() {
        return importTaxonObservationCollection;
    }

    public void setImportTaxonObservationCollection(Collection<ImportTaxonObservation> importTaxonObservationCollection) {
        this.importTaxonObservationCollection = importTaxonObservationCollection;
    }

    public ImportDataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(ImportDataset datasetKey) {
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
        if (!(object instanceof ImportSite)) {
            return false;
        }
        ImportSite other = (ImportSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportSite[ id=" + id + " ]";
    }
    
}
