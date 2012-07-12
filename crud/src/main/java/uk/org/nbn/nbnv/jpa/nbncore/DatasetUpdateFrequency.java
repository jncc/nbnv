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
@Table(name = "DatasetUpdateFrequency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetUpdateFrequency.findAll", query = "SELECT d FROM DatasetUpdateFrequency d"),
    @NamedQuery(name = "DatasetUpdateFrequency.findByCode", query = "SELECT d FROM DatasetUpdateFrequency d WHERE d.code = :code"),
    @NamedQuery(name = "DatasetUpdateFrequency.findByLabel", query = "SELECT d FROM DatasetUpdateFrequency d WHERE d.label = :label")})
public class DatasetUpdateFrequency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "updateFrequency")
    private Collection<Dataset> datasetCollection;

    public DatasetUpdateFrequency() {
    }

    public DatasetUpdateFrequency(String code) {
        this.code = code;
    }

    public DatasetUpdateFrequency(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<Dataset> getDatasetCollection() {
        return datasetCollection;
    }

    public void setDatasetCollection(Collection<Dataset> datasetCollection) {
        this.datasetCollection = datasetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetUpdateFrequency)) {
            return false;
        }
        DatasetUpdateFrequency other = (DatasetUpdateFrequency) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DatasetUpdateFrequency[ code=" + code + " ]";
    }
    
}
