/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonDesignation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonDesignation.findAll", query = "SELECT t FROM TaxonDesignation t"),
    @NamedQuery(name = "TaxonDesignation.findByTaxonVersionKey", query = "SELECT t FROM TaxonDesignation t WHERE t.taxonDesignationPK.taxonVersionKey = :taxonVersionKey"),
    @NamedQuery(name = "TaxonDesignation.findByDesignationID", query = "SELECT t FROM TaxonDesignation t WHERE t.taxonDesignationPK.designationID = :designationID"),
    @NamedQuery(name = "TaxonDesignation.findByStartDate", query = "SELECT t FROM TaxonDesignation t WHERE t.startDate = :startDate"),
    @NamedQuery(name = "TaxonDesignation.findByEndDate", query = "SELECT t FROM TaxonDesignation t WHERE t.endDate = :endDate"),
    @NamedQuery(name = "TaxonDesignation.findBySource", query = "SELECT t FROM TaxonDesignation t WHERE t.source = :source"),
    @NamedQuery(name = "TaxonDesignation.findByStatusConstraint", query = "SELECT t FROM TaxonDesignation t WHERE t.statusConstraint = :statusConstraint")})
public class TaxonDesignation implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaxonDesignationPK taxonDesignationPK;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 2147483647)
    @Column(name = "source")
    private String source;
    @Size(max = 2147483647)
    @Column(name = "statusConstraint")
    private String statusConstraint;
    @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Taxon taxon;
    @JoinColumn(name = "designationID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Designation designation;

    public TaxonDesignation() {
    }

    public TaxonDesignation(TaxonDesignationPK taxonDesignationPK) {
        this.taxonDesignationPK = taxonDesignationPK;
    }

    public TaxonDesignation(String taxonVersionKey, int designationID) {
        this.taxonDesignationPK = new TaxonDesignationPK(taxonVersionKey, designationID);
    }

    public TaxonDesignationPK getTaxonDesignationPK() {
        return taxonDesignationPK;
    }

    public void setTaxonDesignationPK(TaxonDesignationPK taxonDesignationPK) {
        this.taxonDesignationPK = taxonDesignationPK;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatusConstraint() {
        return statusConstraint;
    }

    public void setStatusConstraint(String statusConstraint) {
        this.statusConstraint = statusConstraint;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonDesignationPK != null ? taxonDesignationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonDesignation)) {
            return false;
        }
        TaxonDesignation other = (TaxonDesignation) object;
        if ((this.taxonDesignationPK == null && other.taxonDesignationPK != null) || (this.taxonDesignationPK != null && !this.taxonDesignationPK.equals(other.taxonDesignationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonDesignation[ taxonDesignationPK=" + taxonDesignationPK + " ]";
    }
    
}
