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
@Table(name = "KnownLanguage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnownLanguage.findAll", query = "SELECT k FROM KnownLanguage k"),
    @NamedQuery(name = "KnownLanguage.findByCodedLanguageID", query = "SELECT k FROM KnownLanguage k WHERE k.codedLanguageID = :codedLanguageID"),
    @NamedQuery(name = "KnownLanguage.findByLanguageName", query = "SELECT k FROM KnownLanguage k WHERE k.languageName = :languageName"),
    @NamedQuery(name = "KnownLanguage.findBySortOrder", query = "SELECT k FROM KnownLanguage k WHERE k.sortOrder = :sortOrder")})
public class KnownLanguage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codedLanguageID")
    private String codedLanguageID;
    @Basic(optional = false)
    @Column(name = "languageName")
    private String languageName;
    @Basic(optional = false)
    @Column(name = "sortOrder")
    private int sortOrder;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lang")
    private Collection<Taxon> taxonCollection;

    public KnownLanguage() {
    }

    public KnownLanguage(String codedLanguageID) {
        this.codedLanguageID = codedLanguageID;
    }

    public KnownLanguage(String codedLanguageID, String languageName, int sortOrder) {
        this.codedLanguageID = codedLanguageID;
        this.languageName = languageName;
        this.sortOrder = sortOrder;
    }

    public String getCodedLanguageID() {
        return codedLanguageID;
    }

    public void setCodedLanguageID(String codedLanguageID) {
        this.codedLanguageID = codedLanguageID;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @XmlTransient
    public Collection<Taxon> getTaxonCollection() {
        return taxonCollection;
    }

    public void setTaxonCollection(Collection<Taxon> taxonCollection) {
        this.taxonCollection = taxonCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codedLanguageID != null ? codedLanguageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnownLanguage)) {
            return false;
        }
        KnownLanguage other = (KnownLanguage) object;
        if ((this.codedLanguageID == null && other.codedLanguageID != null) || (this.codedLanguageID != null && !this.codedLanguageID.equals(other.codedLanguageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.KnownLanguage[ codedLanguageID=" + codedLanguageID + " ]";
    }
    
}
