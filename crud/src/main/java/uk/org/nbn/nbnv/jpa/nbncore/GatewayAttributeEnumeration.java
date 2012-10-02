/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "GatewayAttributeEnumeration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GatewayAttributeEnumeration.findAll", query = "SELECT g FROM GatewayAttributeEnumeration g"),
    @NamedQuery(name = "GatewayAttributeEnumeration.findByGatewayAttributeID", query = "SELECT g FROM GatewayAttributeEnumeration g WHERE g.gatewayAttributeEnumerationPK.gatewayAttributeID = :gatewayAttributeID"),
    @NamedQuery(name = "GatewayAttributeEnumeration.findByEnumValue", query = "SELECT g FROM GatewayAttributeEnumeration g WHERE g.gatewayAttributeEnumerationPK.enumValue = :enumValue"),
    @NamedQuery(name = "GatewayAttributeEnumeration.findByLabel", query = "SELECT g FROM GatewayAttributeEnumeration g WHERE g.label = :label"),
    @NamedQuery(name = "GatewayAttributeEnumeration.findByDescription", query = "SELECT g FROM GatewayAttributeEnumeration g WHERE g.description = :description")})
public class GatewayAttributeEnumeration implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GatewayAttributeEnumerationPK gatewayAttributeEnumerationPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "gatewayAttributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GatewayAttribute gatewayAttribute;

    public GatewayAttributeEnumeration() {
    }

    public GatewayAttributeEnumeration(GatewayAttributeEnumerationPK gatewayAttributeEnumerationPK) {
        this.gatewayAttributeEnumerationPK = gatewayAttributeEnumerationPK;
    }

    public GatewayAttributeEnumeration(GatewayAttributeEnumerationPK gatewayAttributeEnumerationPK, String label) {
        this.gatewayAttributeEnumerationPK = gatewayAttributeEnumerationPK;
        this.label = label;
    }

    public GatewayAttributeEnumeration(int gatewayAttributeID, int enumValue) {
        this.gatewayAttributeEnumerationPK = new GatewayAttributeEnumerationPK(gatewayAttributeID, enumValue);
    }

    public GatewayAttributeEnumerationPK getGatewayAttributeEnumerationPK() {
        return gatewayAttributeEnumerationPK;
    }

    public void setGatewayAttributeEnumerationPK(GatewayAttributeEnumerationPK gatewayAttributeEnumerationPK) {
        this.gatewayAttributeEnumerationPK = gatewayAttributeEnumerationPK;
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

    public GatewayAttribute getGatewayAttribute() {
        return gatewayAttribute;
    }

    public void setGatewayAttribute(GatewayAttribute gatewayAttribute) {
        this.gatewayAttribute = gatewayAttribute;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gatewayAttributeEnumerationPK != null ? gatewayAttributeEnumerationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GatewayAttributeEnumeration)) {
            return false;
        }
        GatewayAttributeEnumeration other = (GatewayAttributeEnumeration) object;
        if ((this.gatewayAttributeEnumerationPK == null && other.gatewayAttributeEnumerationPK != null) || (this.gatewayAttributeEnumerationPK != null && !this.gatewayAttributeEnumerationPK.equals(other.gatewayAttributeEnumerationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.GatewayAttributeEnumeration[ gatewayAttributeEnumerationPK=" + gatewayAttributeEnumerationPK + " ]";
    }
    
}
