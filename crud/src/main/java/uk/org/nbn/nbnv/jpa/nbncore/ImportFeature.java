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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * @author felix mason
 */
@Entity
@Table(name = "ImportFeature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportFeature.findAll", query = "SELECT i FROM ImportFeature i"),
    @NamedQuery(name = "ImportFeature.findById", query = "SELECT i FROM ImportFeature i WHERE i.id = :id"),
    @NamedQuery(name = "ImportFeature.findByIdentifier", query = "SELECT i FROM ImportFeature i WHERE i.identifier = :identifier")})
public class ImportFeature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "geom")
    private byte[] geom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "identifier")
    private String identifier;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "featureID")
    private Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "featureID")
    private Collection<ImportTaxonObservation> importTaxonObservationCollection;

    public ImportFeature() {
    }

    public ImportFeature(Integer id) {
        this.id = id;
    }

    public ImportFeature(Integer id, byte[] geom, String identifier) {
        this.id = id;
        this.geom = geom;
        this.identifier = identifier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportFeature)) {
            return false;
        }
        ImportFeature other = (ImportFeature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportFeature[ id=" + id + " ]";
    }
    
}
