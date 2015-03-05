/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author felix mason
 */
@Entity
@Table(name = "ImportFeatures")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportFeatures.findAll", query = "SELECT i FROM ImportFeatures i")
})
public class ImportFeatures implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "featureID")
    private Integer featureID;

    public ImportFeatures() {
    }

    public ImportFeatures(Integer featureID) {
        this.featureID = featureID;
    }

    public Integer featureID() {
        return featureID;
    }

    public void setId(Integer featureID) {
        this.featureID = featureID;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportFeature[ featureID=" + featureID + " ]";
    }
    
}
