/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Paul Gilbertson
 */
@Embeddable
public class GatewayAttributeEnumerationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "gatewayAttributeID")
    private int gatewayAttributeID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enumValue")
    private int enumValue;

    public GatewayAttributeEnumerationPK() {
    }

    public GatewayAttributeEnumerationPK(int gatewayAttributeID, int enumValue) {
        this.gatewayAttributeID = gatewayAttributeID;
        this.enumValue = enumValue;
    }

    public int getGatewayAttributeID() {
        return gatewayAttributeID;
    }

    public void setGatewayAttributeID(int gatewayAttributeID) {
        this.gatewayAttributeID = gatewayAttributeID;
    }

    public int getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(int enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gatewayAttributeID;
        hash += (int) enumValue;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GatewayAttributeEnumerationPK)) {
            return false;
        }
        GatewayAttributeEnumerationPK other = (GatewayAttributeEnumerationPK) object;
        if (this.gatewayAttributeID != other.gatewayAttributeID) {
            return false;
        }
        if (this.enumValue != other.enumValue) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.GatewayAttributeEnumerationPK[ gatewayAttributeID=" + gatewayAttributeID + ", enumValue=" + enumValue + " ]";
    }
    
}
