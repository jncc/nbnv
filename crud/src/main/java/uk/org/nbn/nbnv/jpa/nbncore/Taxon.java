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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "Taxon")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taxon.findAll", query = "SELECT t FROM Taxon t"),
    @NamedQuery(name = "Taxon.findByTaxonVersionKey", query = "SELECT t FROM Taxon t WHERE t.taxonVersionKey = :taxonVersionKey"),
    @NamedQuery(name = "Taxon.findByName", query = "SELECT t FROM Taxon t WHERE t.name = :name"),
    @NamedQuery(name = "Taxon.findByAuthority", query = "SELECT t FROM Taxon t WHERE t.authority = :authority"),
    @NamedQuery(name = "Taxon.findByTaxonCode", query = "SELECT t FROM Taxon t WHERE t.taxonCode = :taxonCode")})
public class Taxon implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 181)
    @Column(name = "name")
    private String name;
    @Size(max = 80)
    @Column(name = "authority")
    private String authority;
    @Size(max = 5)
    @Column(name = "taxonCode")
    private String taxonCode;
    @JoinTable(name = "TaxonNavigation", joinColumns = {
        @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey")}, inverseJoinColumns = {
        @JoinColumn(name = "taxonNavigationGroupKey", referencedColumnName = "key")})
    @ManyToMany
    private Collection<TaxonGroup> taxonGroupCollection;
    @JoinTable(name = "TaxonAggregate", joinColumns = {
        @JoinColumn(name = "aggregateTaxonVersionKey", referencedColumnName = "taxonVersionKey")}, inverseJoinColumns = {
        @JoinColumn(name = "componentTaxonVersionKey", referencedColumnName = "taxonVersionKey")})
    @ManyToMany
    private Collection<Taxon> taxonCollection;
    @ManyToMany(mappedBy = "taxonCollection")
    private Collection<Taxon> taxonCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxon")
    private Collection<TaxonDesignation> taxonDesignationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxon")
    private Collection<TaxonAttribute> taxonAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxon")
    private Collection<TaxonObservation> taxonObservationCollection;
    @JoinColumn(name = "taxonVersionFormKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private TaxonVersionForm taxonVersionForm;
    @JoinColumn(name = "taxonRankID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TaxonRank taxonRank;
    @JoinColumn(name = "taxonNameStatusKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private TaxonNameStatus taxonNameStatus;
    @JoinColumn(name = "taxonOutputGroupKey", referencedColumnName = "key")
    @ManyToOne
    private TaxonGroup taxonGroup;
    @OneToMany(mappedBy = "taxon")
    private Collection<Taxon> taxonCollection2;
    @JoinColumn(name = "commonNameTaxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne
    private Taxon taxon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxon1")
    private Collection<Taxon> taxonCollection3;
    @JoinColumn(name = "pTaxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne(optional = false)
    private Taxon taxon1;
    @JoinColumn(name = "organismKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private Organism organism;
    @JoinColumn(name = "languageKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private Language language;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxon")
    private Collection<RecordingEntity> recordingEntityCollection;
    @OneToMany(mappedBy = "taxon")
    private Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection;

    public Taxon() {
    }

    public Taxon(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public Taxon(String taxonVersionKey, String name) {
        this.taxonVersionKey = taxonVersionKey;
        this.name = name;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTaxonCode() {
        return taxonCode;
    }

    public void setTaxonCode(String taxonCode) {
        this.taxonCode = taxonCode;
    }

    @XmlTransient
    public Collection<TaxonGroup> getTaxonGroupCollection() {
        return taxonGroupCollection;
    }

    public void setTaxonGroupCollection(Collection<TaxonGroup> taxonGroupCollection) {
        this.taxonGroupCollection = taxonGroupCollection;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection() {
        return taxonCollection;
    }

    public void setTaxonCollection(Collection<Taxon> taxonCollection) {
        this.taxonCollection = taxonCollection;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection1() {
        return taxonCollection1;
    }

    public void setTaxonCollection1(Collection<Taxon> taxonCollection1) {
        this.taxonCollection1 = taxonCollection1;
    }

    @XmlTransient
    public Collection<TaxonDesignation> getTaxonDesignationCollection() {
        return taxonDesignationCollection;
    }

    public void setTaxonDesignationCollection(Collection<TaxonDesignation> taxonDesignationCollection) {
        this.taxonDesignationCollection = taxonDesignationCollection;
    }

    @XmlTransient
    public Collection<TaxonAttribute> getTaxonAttributeCollection() {
        return taxonAttributeCollection;
    }

    public void setTaxonAttributeCollection(Collection<TaxonAttribute> taxonAttributeCollection) {
        this.taxonAttributeCollection = taxonAttributeCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    public TaxonVersionForm getTaxonVersionForm() {
        return taxonVersionForm;
    }

    public void setTaxonVersionForm(TaxonVersionForm taxonVersionForm) {
        this.taxonVersionForm = taxonVersionForm;
    }

    public TaxonRank getTaxonRank() {
        return taxonRank;
    }

    public void setTaxonRank(TaxonRank taxonRank) {
        this.taxonRank = taxonRank;
    }

    public TaxonNameStatus getTaxonNameStatus() {
        return taxonNameStatus;
    }

    public void setTaxonNameStatus(TaxonNameStatus taxonNameStatus) {
        this.taxonNameStatus = taxonNameStatus;
    }

    public TaxonGroup getTaxonGroup() {
        return taxonGroup;
    }

    public void setTaxonGroup(TaxonGroup taxonGroup) {
        this.taxonGroup = taxonGroup;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection2() {
        return taxonCollection2;
    }

    public void setTaxonCollection2(Collection<Taxon> taxonCollection2) {
        this.taxonCollection2 = taxonCollection2;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection3() {
        return taxonCollection3;
    }

    public void setTaxonCollection3(Collection<Taxon> taxonCollection3) {
        this.taxonCollection3 = taxonCollection3;
    }

    public Taxon getTaxon1() {
        return taxon1;
    }

    public void setTaxon1(Taxon taxon1) {
        this.taxon1 = taxon1;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @XmlTransient
    public Collection<RecordingEntity> getRecordingEntityCollection() {
        return recordingEntityCollection;
    }

    public void setRecordingEntityCollection(Collection<RecordingEntity> recordingEntityCollection) {
        this.recordingEntityCollection = recordingEntityCollection;
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
        hash += (taxonVersionKey != null ? taxonVersionKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taxon)) {
            return false;
        }
        Taxon other = (Taxon) object;
        if ((this.taxonVersionKey == null && other.taxonVersionKey != null) || (this.taxonVersionKey != null && !this.taxonVersionKey.equals(other.taxonVersionKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Taxon[ taxonVersionKey=" + taxonVersionKey + " ]";
    }
    
}
