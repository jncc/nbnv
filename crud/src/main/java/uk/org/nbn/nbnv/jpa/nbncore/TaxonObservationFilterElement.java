/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
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
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private TaxonObservationFilterElementType taxonObservationFilterElementType;
    @JoinColumn(name = "filterID", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private TaxonObservationFilter taxonObservationFilter;
    @JoinColumn(name = "filterDatasetKey", referencedColumnName = "datasetKey")
    @ManyToOne
    private TaxonDataset taxonDataset;
    @JoinColumn(name = "filterTaxon", referencedColumnName = "taxonVersionKey")
    @ManyToOne
    private Taxon taxon;
    @JoinColumn(name = "filterSiteBoundary", referencedColumnName = "featureID")
    @ManyToOne
    private SiteBoundary siteBoundary;

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

    public TaxonObservationFilterElementType getTaxonObservationFilterElementType() {
        return taxonObservationFilterElementType;
    }

    public void setTaxonObservationFilterElementType(TaxonObservationFilterElementType taxonObservationFilterElementType) {
        this.taxonObservationFilterElementType = taxonObservationFilterElementType;
    }

    public TaxonObservationFilter getTaxonObservationFilter() {
        return taxonObservationFilter;
    }

    public void setTaxonObservationFilter(TaxonObservationFilter taxonObservationFilter) {
        this.taxonObservationFilter = taxonObservationFilter;
    }

    public TaxonDataset getTaxonDataset() {
        return taxonDataset;
    }

    public void setTaxonDataset(TaxonDataset taxonDataset) {
        this.taxonDataset = taxonDataset;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    public SiteBoundary getSiteBoundary() {
        return siteBoundary;
    }

    public void setSiteBoundary(SiteBoundary siteBoundary) {
        this.siteBoundary = siteBoundary;
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
