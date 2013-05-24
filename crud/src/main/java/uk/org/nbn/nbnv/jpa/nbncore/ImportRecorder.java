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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author felix mason
 */
@Entity
@Table(name = "ImportRecorder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportRecorder.findAll", query = "SELECT i FROM ImportRecorder i"),
    @NamedQuery(name = "ImportRecorder.findById", query = "SELECT i FROM ImportRecorder i WHERE i.id = :id"),
    @NamedQuery(name = "ImportRecorder.findByName", query = "SELECT i FROM ImportRecorder i WHERE i.name = :name")})
public class ImportRecorder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "recorderID")
    private Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection;
    @OneToMany(mappedBy = "determinerID")
    private Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection1;
    @OneToMany(mappedBy = "recorderID")
    private Collection<ImportTaxonObservation> importTaxonObservationCollection;
    @OneToMany(mappedBy = "determinerID")
    private Collection<ImportTaxonObservation> importTaxonObservationCollection1;

    public ImportRecorder() {
    }

    public ImportRecorder(Integer id) {
        this.id = id;
    }

    public ImportRecorder(Integer id, String name) {
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
    public Collection<ImportTaxonObservationPublic> getImportTaxonObservationPublicCollection() {
        return importTaxonObservationPublicCollection;
    }

    public void setImportTaxonObservationPublicCollection(Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection) {
        this.importTaxonObservationPublicCollection = importTaxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<ImportTaxonObservationPublic> getImportTaxonObservationPublicCollection1() {
        return importTaxonObservationPublicCollection1;
    }

    public void setImportTaxonObservationPublicCollection1(Collection<ImportTaxonObservationPublic> importTaxonObservationPublicCollection1) {
        this.importTaxonObservationPublicCollection1 = importTaxonObservationPublicCollection1;
    }

    @XmlTransient
    public Collection<ImportTaxonObservation> getImportTaxonObservationCollection() {
        return importTaxonObservationCollection;
    }

    public void setImportTaxonObservationCollection(Collection<ImportTaxonObservation> importTaxonObservationCollection) {
        this.importTaxonObservationCollection = importTaxonObservationCollection;
    }

    @XmlTransient
    public Collection<ImportTaxonObservation> getImportTaxonObservationCollection1() {
        return importTaxonObservationCollection1;
    }

    public void setImportTaxonObservationCollection1(Collection<ImportTaxonObservation> importTaxonObservationCollection1) {
        this.importTaxonObservationCollection1 = importTaxonObservationCollection1;
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
        if (!(object instanceof ImportRecorder)) {
            return false;
        }
        ImportRecorder other = (ImportRecorder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportRecorder[ id=" + id + " ]";
    }
    
}
