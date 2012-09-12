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
    @NamedQuery(name = "SiteBoundaryType.findBySiteBoundaryTypeID", query = "SELECT s FROM SiteBoundaryType s WHERE s.siteBoundaryTypeID = :siteBoundaryTypeID"),
    @NamedQuery(name = "SiteBoundaryType.findBySiteTypeName", query = "SELECT s FROM SiteBoundaryType s WHERE s.siteTypeName = :siteTypeName"),
    @NamedQuery(name = "SiteBoundaryType.findBySiteTypeCode", query = "SELECT s FROM SiteBoundaryType s WHERE s.siteTypeCode = :siteTypeCode"),
    @NamedQuery(name = "SiteBoundaryType.findByXmlEnumValue", query = "SELECT s FROM SiteBoundaryType s WHERE s.xmlEnumValue = :xmlEnumValue")})
public class SiteBoundaryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "siteBoundaryTypeID")
    private Integer siteBoundaryTypeID;
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
    @OneToMany(mappedBy = "parentTypeID")
    private Collection<SiteBoundaryType> siteBoundaryTypeCollection;
    @JoinColumn(name = "parentTypeID", referencedColumnName = "siteBoundaryTypeID")
    @ManyToOne
    private SiteBoundaryType parentTypeID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundaryCategory")
    private Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection;

    public SiteBoundaryType() {
    }

    public SiteBoundaryType(Integer siteBoundaryTypeID) {
        this.siteBoundaryTypeID = siteBoundaryTypeID;
    }

    public SiteBoundaryType(Integer siteBoundaryTypeID, String siteTypeName) {
        this.siteBoundaryTypeID = siteBoundaryTypeID;
        this.siteTypeName = siteTypeName;
    }

    public Integer getSiteBoundaryTypeID() {
        return siteBoundaryTypeID;
    }

    public void setSiteBoundaryTypeID(Integer siteBoundaryTypeID) {
        this.siteBoundaryTypeID = siteBoundaryTypeID;
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

    public SiteBoundaryType getParentTypeID() {
        return parentTypeID;
    }

    public void setParentTypeID(SiteBoundaryType parentTypeID) {
        this.parentTypeID = parentTypeID;
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
        hash += (siteBoundaryTypeID != null ? siteBoundaryTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiteBoundaryType)) {
            return false;
        }
        SiteBoundaryType other = (SiteBoundaryType) object;
        if ((this.siteBoundaryTypeID == null && other.siteBoundaryTypeID != null) || (this.siteBoundaryTypeID != null && !this.siteBoundaryTypeID.equals(other.siteBoundaryTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryType[ siteBoundaryTypeID=" + siteBoundaryTypeID + " ]";
    }
    
}
