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
@Table(name = "Taxon")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taxon.findAll", query = "SELECT t FROM Taxon t"),
    @NamedQuery(name = "Taxon.findByTaxonVersionKey", query = "SELECT t FROM Taxon t WHERE t.taxonVersionKey = :taxonVersionKey"),
    @NamedQuery(name = "Taxon.findByTaxonName", query = "SELECT t FROM Taxon t WHERE t.taxonName = :taxonName"),
    @NamedQuery(name = "Taxon.findByTaxonAuthority", query = "SELECT t FROM Taxon t WHERE t.taxonAuthority = :taxonAuthority")})
public class Taxon implements Serializable {
    @JoinColumn(name = "taxonNavigationGroupKey", referencedColumnName = "taxonGroupKey")
    @ManyToOne
    private TaxonGroup taxonNavigationGroupKey;
    @JoinColumn(name = "taxonOutputGroupKey", referencedColumnName = "taxonGroupKey")
    @ManyToOne
    private TaxonGroup taxonOutputGroupKey;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Column(name = "taxonName")
    private String taxonName;
    @Column(name = "taxonAuthority")
    private String taxonAuthority;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonVersionKey")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonVersionKey")
    private Collection<TaxonObservation> taxonObservationCollection;
    @OneToMany(mappedBy = "prefnameTaxonVersionKey")
    private Collection<Taxon> taxonCollection;
    @JoinColumn(name = "prefnameTaxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne
    private Taxon prefnameTaxonVersionKey;
    @JoinColumn(name = "lang", referencedColumnName = "codedLanguageID")
    @ManyToOne(optional = false)
    private KnownLanguage lang;

    public Taxon() {
    }

    public Taxon(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    public String getTaxonAuthority() {
        return taxonAuthority;
    }

    public void setTaxonAuthority(String taxonAuthority) {
        this.taxonAuthority = taxonAuthority;
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

    @XmlTransient
    public Collection<Taxon> getTaxonCollection() {
        return taxonCollection;
    }

    public void setTaxonCollection(Collection<Taxon> taxonCollection) {
        this.taxonCollection = taxonCollection;
    }

    public Taxon getPrefnameTaxonVersionKey() {
        return prefnameTaxonVersionKey;
    }

    public void setPrefnameTaxonVersionKey(Taxon prefnameTaxonVersionKey) {
        this.prefnameTaxonVersionKey = prefnameTaxonVersionKey;
    }

    public KnownLanguage getLang() {
        return lang;
    }

    public void setLang(KnownLanguage lang) {
        this.lang = lang;
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

    public TaxonGroup getTaxonNavigationGroupKey() {
        return taxonNavigationGroupKey;
    }

    public void setTaxonNavigationGroupKey(TaxonGroup taxonNavigationGroupKey) {
        this.taxonNavigationGroupKey = taxonNavigationGroupKey;
    }

    public TaxonGroup getTaxonOutputGroupKey() {
        return taxonOutputGroupKey;
    }

    public void setTaxonOutputGroupKey(TaxonGroup taxonOutputGroupKey) {
        this.taxonOutputGroupKey = taxonOutputGroupKey;
    }
    
}
