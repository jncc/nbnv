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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "TaxonObservationFilter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationFilter.findAll", query = "SELECT t FROM TaxonObservationFilter t"),
    @NamedQuery(name = "TaxonObservationFilter.findById", query = "SELECT t FROM TaxonObservationFilter t WHERE t.id = :id"),
    @NamedQuery(name = "TaxonObservationFilter.findByFilterJSON", query = "SELECT t FROM TaxonObservationFilter t WHERE t.filterJSON = :filterJSON"),
    @NamedQuery(name = "TaxonObservationFilter.findByFilterText", query = "SELECT t FROM TaxonObservationFilter t WHERE t.filterText = :filterText")})
public class TaxonObservationFilter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "filterJSON")
    private String filterJSON;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "filterText")
    private String filterText;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taxonObservationFilter")
    private TaxonObservationDownload taxonObservationDownload;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taxonObservationFilter")
    private UserAccessRequest userAccessRequest;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taxonObservationFilter")
    private OrganisationAccessRequest organisationAccessRequest;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filterID")
    private Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection;

    public TaxonObservationFilter() {
    }

    public TaxonObservationFilter(Integer id) {
        this.id = id;
    }

    public TaxonObservationFilter(Integer id, String filterJSON, String filterText) {
        this.id = id;
        this.filterJSON = filterJSON;
        this.filterText = filterText;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilterJSON() {
        return filterJSON;
    }

    public void setFilterJSON(String filterJSON) {
        this.filterJSON = filterJSON;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public TaxonObservationDownload getTaxonObservationDownload() {
        return taxonObservationDownload;
    }

    public void setTaxonObservationDownload(TaxonObservationDownload taxonObservationDownload) {
        this.taxonObservationDownload = taxonObservationDownload;
    }

    public UserAccessRequest getUserAccessRequest() {
        return userAccessRequest;
    }

    public void setUserAccessRequest(UserAccessRequest userAccessRequest) {
        this.userAccessRequest = userAccessRequest;
    }

    public OrganisationAccessRequest getOrganisationAccessRequest() {
        return organisationAccessRequest;
    }

    public void setOrganisationAccessRequest(OrganisationAccessRequest organisationAccessRequest) {
        this.organisationAccessRequest = organisationAccessRequest;
    }

    @XmlTransient
    public Collection<TaxonObservationFilterElement> getTaxonObservationFilterElementCollection() {
        return taxonObservationFilterElementCollection;
    }

    public void setTaxonObservationFilterElementCollection(Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection) {
        this.taxonObservationFilterElementCollection = taxonObservationFilterElementCollection;
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
        if (!(object instanceof TaxonObservationFilter)) {
            return false;
        }
        TaxonObservationFilter other = (TaxonObservationFilter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationFilter[ id=" + id + " ]";
    }
    
}
