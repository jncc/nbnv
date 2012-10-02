/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Designation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Designation.findAll", query = "SELECT d FROM Designation d"),
    @NamedQuery(name = "Designation.findById", query = "SELECT d FROM Designation d WHERE d.id = :id"),
    @NamedQuery(name = "Designation.findByName", query = "SELECT d FROM Designation d WHERE d.name = :name"),
    @NamedQuery(name = "Designation.findByLabel", query = "SELECT d FROM Designation d WHERE d.label = :label"),
    @NamedQuery(name = "Designation.findByCode", query = "SELECT d FROM Designation d WHERE d.code = :code"),
    @NamedQuery(name = "Designation.findByDescription", query = "SELECT d FROM Designation d WHERE d.description = :description"),
    @NamedQuery(name = "Designation.findByGeographicalConstraint", query = "SELECT d FROM Designation d WHERE d.geographicalConstraint = :geographicalConstraint")})
public class Designation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "geographicalConstraint")
    private String geographicalConstraint;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "designation")
    private Collection<TaxonDesignation> taxonDesignationCollection;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne
    private Feature featureID;
    @JoinColumn(name = "designationCategoryID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DesignationCategory designationCategoryID;

    public Designation() {
    }

    public Designation(Integer id) {
        this.id = id;
    }

    public Designation(Integer id, String name, String label, String code) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeographicalConstraint() {
        return geographicalConstraint;
    }

    public void setGeographicalConstraint(String geographicalConstraint) {
        this.geographicalConstraint = geographicalConstraint;
    }

    @XmlTransient
    public Collection<TaxonDesignation> getTaxonDesignationCollection() {
        return taxonDesignationCollection;
    }

    public void setTaxonDesignationCollection(Collection<TaxonDesignation> taxonDesignationCollection) {
        this.taxonDesignationCollection = taxonDesignationCollection;
    }

    public Feature getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Feature featureID) {
        this.featureID = featureID;
    }

    public DesignationCategory getDesignationCategoryID() {
        return designationCategoryID;
    }

    public void setDesignationCategoryID(DesignationCategory designationCategoryID) {
        this.designationCategoryID = designationCategoryID;
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
        if (!(object instanceof Designation)) {
            return false;
        }
        Designation other = (Designation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Designation[ id=" + id + " ]";
    }
    
}
