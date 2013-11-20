/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "RecordingEntity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecordingEntity.findAll", query = "SELECT r FROM RecordingEntity r"),
    @NamedQuery(name = "RecordingEntity.findByRecordedName", query = "SELECT r FROM RecordingEntity r WHERE r.name = :recordedName"),
    @NamedQuery(name = "RecordingEntity.findByDangerous", query = "SELECT r FROM RecordingEntity r WHERE r.dangerous = :dangerous")})
public class RecordingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String recordedName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dangerous")
    private boolean dangerous;
    @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne(optional = false)
    private Taxon taxon;

    public RecordingEntity() {
    }

    public RecordingEntity(String recordedName) {
        this.recordedName = recordedName;
    }

    public RecordingEntity(String recordedName, boolean dangerous) {
        this.recordedName = recordedName;
        this.dangerous = dangerous;
    }

    public String getRecordedName() {
        return recordedName;
    }

    public void setRecordedName(String recordedName) {
        this.recordedName = recordedName;
    }

    public boolean getDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordedName != null ? recordedName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecordingEntity)) {
            return false;
        }
        RecordingEntity other = (RecordingEntity) object;
        if ((this.recordedName == null && other.recordedName != null) || (this.recordedName != null && !this.recordedName.equals(other.recordedName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.RecordingEntity[ recordedName=" + recordedName + " ]";
    }
    
}
