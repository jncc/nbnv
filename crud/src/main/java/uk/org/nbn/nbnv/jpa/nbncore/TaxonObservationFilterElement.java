/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonObservationFilterElement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationFilterElement.findAll", query = "SELECT t FROM TaxonObservationFilterElement t"),
    @NamedQuery(name = "TaxonObservationFilterElement.findById", query = "SELECT t FROM TaxonObservationFilterElement t WHERE t.id = :id"),
    @NamedQuery(name = "TaxonObservationFilterElement.findByFilterSensitive", query = "SELECT t FROM TaxonObservationFilterElement t WHERE t.filterSensitive = :filterSensitive"),
    @NamedQuery(name = "TaxonObservationFilterElement.findByFilterSiteBoundaryMatch", query = "SELECT t FROM TaxonObservationFilterElement t WHERE t.filterSiteBoundaryMatch = :filterSiteBoundaryMatch"),
    @NamedQuery(name = "TaxonObservationFilterElement.findByFilterDateStart", query = "SELECT t FROM TaxonObservationFilterElement t WHERE t.filterDateStart = :filterDateStart"),
    @NamedQuery(name = "TaxonObservationFilterElement.findByFilterDateEnd", query = "SELECT t FROM TaxonObservationFilterElement t WHERE t.filterDateEnd = :filterDateEnd")})
public class TaxonObservationFilterElement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "filterSensitive")
    private Integer filterSensitive;
    @Column(name = "filterSiteBoundaryMatch")
    private Integer filterSiteBoundaryMatch;
    @Column(name = "filterDateStart")
    @Temporal(TemporalType.DATE)
    private Date filterDateStart;
    @Column(name = "filterDateEnd")
    @Temporal(TemporalType.DATE)
    private Date filterDateEnd;
    @JoinColumn(name = "filterElementTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TaxonObservationFilterElementType filterElementTypeID;
    @JoinColumn(name = "filterID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TaxonObservationFilter filterID;
    @JoinColumn(name = "filterDatasetKey", referencedColumnName = "datasetKey")
    @ManyToOne
    private TaxonDataset filterDatasetKey;
    @JoinColumn(name = "filterTaxon", referencedColumnName = "taxonVersionKey")
    @ManyToOne
    private Taxon filterTaxon;
    @JoinColumn(name = "filterSiteBoundary", referencedColumnName = "featureID")
    @ManyToOne
    private SiteBoundary filterSiteBoundary;

    public TaxonObservationFilterElement() {
    }

    public TaxonObservationFilterElement(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFilterSensitive() {
        return filterSensitive;
    }

    public void setFilterSensitive(Integer filterSensitive) {
        this.filterSensitive = filterSensitive;
    }

    public Integer getFilterSiteBoundaryMatch() {
        return filterSiteBoundaryMatch;
    }

    public void setFilterSiteBoundaryMatch(Integer filterSiteBoundaryMatch) {
        this.filterSiteBoundaryMatch = filterSiteBoundaryMatch;
    }

    public Date getFilterDateStart() {
        return filterDateStart;
    }

    public void setFilterDateStart(Date filterDateStart) {
        this.filterDateStart = filterDateStart;
    }

    public Date getFilterDateEnd() {
        return filterDateEnd;
    }

    public void setFilterDateEnd(Date filterDateEnd) {
        this.filterDateEnd = filterDateEnd;
    }

    public TaxonObservationFilterElementType getFilterElementTypeID() {
        return filterElementTypeID;
    }

    public void setFilterElementTypeID(TaxonObservationFilterElementType filterElementTypeID) {
        this.filterElementTypeID = filterElementTypeID;
    }

    public TaxonObservationFilter getFilterID() {
        return filterID;
    }

    public void setFilterID(TaxonObservationFilter filterID) {
        this.filterID = filterID;
    }

    public TaxonDataset getFilterDatasetKey() {
        return filterDatasetKey;
    }

    public void setFilterDatasetKey(TaxonDataset filterDatasetKey) {
        this.filterDatasetKey = filterDatasetKey;
    }

    public Taxon getFilterTaxon() {
        return filterTaxon;
    }

    public void setFilterTaxon(Taxon filterTaxon) {
        this.filterTaxon = filterTaxon;
    }

    public SiteBoundary getFilterSiteBoundary() {
        return filterSiteBoundary;
    }

    public void setFilterSiteBoundary(SiteBoundary filterSiteBoundary) {
        this.filterSiteBoundary = filterSiteBoundary;
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
        if (!(object instanceof TaxonObservationFilterElement)) {
            return false;
        }
        TaxonObservationFilterElement other = (TaxonObservationFilterElement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationFilterElement[ id=" + id + " ]";
    }
    
}
