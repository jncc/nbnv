/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Site")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s"),
    @NamedQuery(name = "Site.findBySiteID", query = "SELECT s FROM Site s WHERE s.siteID = :siteID"),
    @NamedQuery(name = "Site.findBySiteKey", query = "SELECT s FROM Site s WHERE s.siteKey = :siteKey"),
    @NamedQuery(name = "Site.findBySiteName", query = "SELECT s FROM Site s WHERE s.siteName = :siteName"),
    @NamedQuery(name = "Site.findByProviderKey", query = "SELECT s FROM Site s WHERE s.providerKey = :providerKey")})
public class Site implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "siteID")
    private Integer siteID;
    @Basic(optional = false)
    @Column(name = "siteKey")
    private String siteKey;
    @Basic(optional = false)
    @Column(name = "siteName")
    private String siteName;
    @Column(name = "providerKey")
    private String providerKey;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private Dataset datasetKey;
    @OneToMany(mappedBy = "siteID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteID")
    private Collection<TaxonObservation> taxonObservationCollection;

    public Site() {
    }

    public Site(Integer siteID) {
        this.siteID = siteID;
    }

    public Site(Integer siteID, String siteKey, String siteName) {
        this.siteID = siteID;
        this.siteKey = siteKey;
        this.siteName = siteName;
    }

    public Integer getSiteID() {
        return siteID;
    }

    public void setSiteID(Integer siteID) {
        this.siteID = siteID;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public Dataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(Dataset datasetKey) {
        this.datasetKey = datasetKey;
    }


    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siteID != null ? siteID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.siteID == null && other.siteID != null) || (this.siteID != null && !this.siteID.equals(other.siteID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Site[ siteID=" + siteID + " ]";
    }
    
}
