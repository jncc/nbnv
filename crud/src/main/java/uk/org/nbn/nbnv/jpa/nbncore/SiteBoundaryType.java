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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "SiteBoundaryType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundaryType.findAll", query = "SELECT s FROM SiteBoundaryType s"),
    @NamedQuery(name = "SiteBoundaryType.findById", query = "SELECT s FROM SiteBoundaryType s WHERE s.id = :id"),
    @NamedQuery(name = "SiteBoundaryType.findBySiteTypeName", query = "SELECT s FROM SiteBoundaryType s WHERE s.siteTypeName = :siteTypeName"),
    @NamedQuery(name = "SiteBoundaryType.findBySiteTypeCode", query = "SELECT s FROM SiteBoundaryType s WHERE s.siteTypeCode = :siteTypeCode"),
    @NamedQuery(name = "SiteBoundaryType.findByXmlEnumValue", query = "SELECT s FROM SiteBoundaryType s WHERE s.xmlEnumValue = :xmlEnumValue")})
public class SiteBoundaryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "siteTypeName")
    private String siteTypeName;
    @Size(max = 30)
    @Column(name = "siteTypeCode")
    private String siteTypeCode;
    @Size(max = 100)
    @Column(name = "xmlEnumValue")
    private String xmlEnumValue;
    @OneToMany(mappedBy = "siteBoundaryType")
    private Collection<SiteBoundaryType> siteBoundaryTypeCollection;
    @JoinColumn(name = "parentID", referencedColumnName = "id")
    @ManyToOne
    private SiteBoundaryType siteBoundaryType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundaryType")
    private Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection;

    public SiteBoundaryType() {
    }

    public SiteBoundaryType(Integer id) {
        this.id = id;
    }

    public SiteBoundaryType(Integer id, String siteTypeName) {
        this.id = id;
        this.siteTypeName = siteTypeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteTypeName() {
        return siteTypeName;
    }

    public void setSiteTypeName(String siteTypeName) {
        this.siteTypeName = siteTypeName;
    }

    public String getSiteTypeCode() {
        return siteTypeCode;
    }

    public void setSiteTypeCode(String siteTypeCode) {
        this.siteTypeCode = siteTypeCode;
    }

    public String getXmlEnumValue() {
        return xmlEnumValue;
    }

    public void setXmlEnumValue(String xmlEnumValue) {
        this.xmlEnumValue = xmlEnumValue;
    }

    @XmlTransient
    public Collection<SiteBoundaryType> getSiteBoundaryTypeCollection() {
        return siteBoundaryTypeCollection;
    }

    public void setSiteBoundaryTypeCollection(Collection<SiteBoundaryType> siteBoundaryTypeCollection) {
        this.siteBoundaryTypeCollection = siteBoundaryTypeCollection;
    }

    public SiteBoundaryType getSiteBoundaryType() {
        return siteBoundaryType;
    }

    public void setSiteBoundaryType(SiteBoundaryType siteBoundaryType) {
        this.siteBoundaryType = siteBoundaryType;
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
        if (!(object instanceof SiteBoundaryType)) {
            return false;
        }
        SiteBoundaryType other = (SiteBoundaryType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryType[ id=" + id + " ]";
    }
    
}
