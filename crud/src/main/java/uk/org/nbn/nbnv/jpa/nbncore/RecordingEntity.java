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
    @NamedQuery(name = "RecordingEntity.findByRecordedName", query = "SELECT r FROM RecordingEntity r WHERE r.name = :name"),
    @NamedQuery(name = "RecordingEntity.findByDangerous", query = "SELECT r FROM RecordingEntity r WHERE r.dangerousName = :dangerousName")})
public class RecordingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pExtendedName")
    private String pExtendedName;    
    @Basic(optional = false)
    @NotNull
    @Column(name = "authority")
    private String authority;    
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonGroup")
    private String taxonGroup;   
    @Basic(optional = false)
    @NotNull
    @Column(name = "taxonRank")
    private String taxonRank; 
    @Basic(optional = false)
    @NotNull
    @Column(name = "dangerousName")
    private String dangerousName;
    @JoinColumn(name = "pTaxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne(optional = false)
    private Taxon taxon; 
    
    public RecordingEntity() {
    }

    public RecordingEntity(String name) {
        this.name = name;
    }

    public RecordingEntity(String name, String dangerousName) {
        this.name = name;
        this.dangerousName = dangerousName;
    }
    
    public RecordingEntity(String taxonVersionKey, String name, String pExtendedName, String authority, String taxonGroup, String taxonRank, String dangerousName) {
        this.taxonVersionKey = taxonVersionKey;
        this.name = name;
        this.pExtendedName = pExtendedName;
        this.authority = authority;
        this.taxonGroup = taxonGroup;
        this.taxonRank = taxonRank;
        this.dangerousName = dangerousName;
    }    

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpExtendedName() {
        return pExtendedName;
    }

    public void setpExtendedName(String pExtendedName) {
        this.pExtendedName = pExtendedName;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTaxonGroup() {
        return taxonGroup;
    }

    public void setTaxonGroup(String taxonGroup) {
        this.taxonGroup = taxonGroup;
    }

    public String getTaxonRank() {
        return taxonRank;
    }

    public void setTaxonRank(String taxonRank) {
        this.taxonRank = taxonRank;
    }

    public String getDangerousName() {
        return dangerousName;
    }

    public void setDangerousName(String dangerousName) {
        this.dangerousName = dangerousName;
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
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecordingEntity)) {
            return false;
        }
        RecordingEntity other = (RecordingEntity) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.RecordingEntity[ recordedName=" + name + " ]";
    }
    
}
