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
@Table(name = "Attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attribute.findAll", query = "SELECT a FROM Attribute a"),
    @NamedQuery(name = "Attribute.findById", query = "SELECT a FROM Attribute a WHERE a.id = :id"),
    @NamedQuery(name = "Attribute.findByLabel", query = "SELECT a FROM Attribute a WHERE a.label = :label"),
    @NamedQuery(name = "Attribute.findByDescription", query = "SELECT a FROM Attribute a WHERE a.description = :description")})
public class Attribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<TaxonObservationAttribute> taxonObservationAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<TaxonAttribute> taxonAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<SiteBoundaryAttribute> siteBoundaryAttributeCollection;
    @JoinColumn(name = "gatewayAttributeID", referencedColumnName = "id")
    @ManyToOne
    private GatewayAttribute gatewayAttribute;
    @JoinColumn(name = "storageTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AttributeStorageType attributeStorageType;
    @JoinColumn(name = "storageLevelID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AttributeStorageLevel attributeStorageLevel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<DatasetAttribute> datasetAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<SampleAttribute> sampleAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<SurveyAttribute> surveyAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<AttributeEnumeration> attributeEnumerationCollection;

    public Attribute() {
    }

    public Attribute(Integer id) {
        this.id = id;
    }

    public Attribute(Integer id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<TaxonObservationAttribute> getTaxonObservationAttributeCollection() {
        return taxonObservationAttributeCollection;
    }

    public void setTaxonObservationAttributeCollection(Collection<TaxonObservationAttribute> taxonObservationAttributeCollection) {
        this.taxonObservationAttributeCollection = taxonObservationAttributeCollection;
    }

    @XmlTransient
    public Collection<TaxonAttribute> getTaxonAttributeCollection() {
        return taxonAttributeCollection;
    }

    public void setTaxonAttributeCollection(Collection<TaxonAttribute> taxonAttributeCollection) {
        this.taxonAttributeCollection = taxonAttributeCollection;
    }

    @XmlTransient
    public Collection<SiteBoundaryAttribute> getSiteBoundaryAttributeCollection() {
        return siteBoundaryAttributeCollection;
    }

    public void setSiteBoundaryAttributeCollection(Collection<SiteBoundaryAttribute> siteBoundaryAttributeCollection) {
        this.siteBoundaryAttributeCollection = siteBoundaryAttributeCollection;
    }

    public GatewayAttribute getGatewayAttribute() {
        return gatewayAttribute;
    }

    public void setGatewayAttribute(GatewayAttribute gatewayAttribute) {
        this.gatewayAttribute = gatewayAttribute;
    }

    public AttributeStorageType getAttributeStorageType() {
        return attributeStorageType;
    }

    public void setAttributeStorageType(AttributeStorageType attributeStorageType) {
        this.attributeStorageType = attributeStorageType;
    }

    public AttributeStorageLevel getAttributeStorageLevel() {
        return attributeStorageLevel;
    }

    public void setAttributeStorageLevel(AttributeStorageLevel attributeStorageLevel) {
        this.attributeStorageLevel = attributeStorageLevel;
    }

    @XmlTransient
    public Collection<DatasetAttribute> getDatasetAttributeCollection() {
        return datasetAttributeCollection;
    }

    public void setDatasetAttributeCollection(Collection<DatasetAttribute> datasetAttributeCollection) {
        this.datasetAttributeCollection = datasetAttributeCollection;
    }

    @XmlTransient
    public Collection<SampleAttribute> getSampleAttributeCollection() {
        return sampleAttributeCollection;
    }

    public void setSampleAttributeCollection(Collection<SampleAttribute> sampleAttributeCollection) {
        this.sampleAttributeCollection = sampleAttributeCollection;
    }

    @XmlTransient
    public Collection<SurveyAttribute> getSurveyAttributeCollection() {
        return surveyAttributeCollection;
    }

    public void setSurveyAttributeCollection(Collection<SurveyAttribute> surveyAttributeCollection) {
        this.surveyAttributeCollection = surveyAttributeCollection;
    }

    @XmlTransient
    public Collection<AttributeEnumeration> getAttributeEnumerationCollection() {
        return attributeEnumerationCollection;
    }

    public void setAttributeEnumerationCollection(Collection<AttributeEnumeration> attributeEnumerationCollection) {
        this.attributeEnumerationCollection = attributeEnumerationCollection;
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
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute other = (Attribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Attribute[ id=" + id + " ]";
    }
    
}
