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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author felix mason
 */
@Entity
@Table(name = "ImportTaxonDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportTaxonDataset.findAll", query = "SELECT i FROM ImportTaxonDataset i"),
    @NamedQuery(name = "ImportTaxonDataset.findByDatasetKey", query = "SELECT i FROM ImportTaxonDataset i WHERE i.datasetKey = :datasetKey"),
    @NamedQuery(name = "ImportTaxonDataset.findByPublicResolutionID", query = "SELECT i FROM ImportTaxonDataset i WHERE i.publicResolutionID = :publicResolutionID"),
    @NamedQuery(name = "ImportTaxonDataset.findByAllowRecordValidation", query = "SELECT i FROM ImportTaxonDataset i WHERE i.allowRecordValidation = :allowRecordValidation"),
    @NamedQuery(name = "ImportTaxonDataset.findByPublicAttribute", query = "SELECT i FROM ImportTaxonDataset i WHERE i.publicAttribute = :publicAttribute")})
public class ImportTaxonDataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publicResolutionID")
    private int publicResolutionID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowRecordValidation")
    private boolean allowRecordValidation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publicAttribute")
    private boolean publicAttribute;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publicRecorder")
    private boolean publicRecorder;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetKey")
    private Collection<ImportSurvey> importSurveyCollection;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ImportDataset importDataset;

    public ImportTaxonDataset() {
    }

    public ImportTaxonDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public ImportTaxonDataset(String datasetKey, int publicResolutionID, boolean allowRecordValidation, boolean publicAttribute) {
        this.datasetKey = datasetKey;
        this.publicResolutionID = publicResolutionID;
        this.allowRecordValidation = allowRecordValidation;
        this.publicAttribute = publicAttribute;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public int getPublicResolutionID() {
        return publicResolutionID;
    }

    public void setPublicResolutionID(int publicResolutionID) {
        this.publicResolutionID = publicResolutionID;
    }

    public boolean getAllowRecordValidation() {
        return allowRecordValidation;
    }

    public void setAllowRecordValidation(boolean allowRecordValidation) {
        this.allowRecordValidation = allowRecordValidation;
    }

    public boolean getPublicAttribute() {
        return publicAttribute;
    }

    public void setPublicAttribute(boolean publicAttribute) {
        this.publicAttribute = publicAttribute;
    }
    
    public boolean getPublicRecorder() {
        return publicRecorder;
    }

    public void setPublicRecorder(boolean publicRecorder) {
        this.publicRecorder = publicRecorder;
    }
    
    @XmlTransient
    public Collection<ImportSurvey> getImportSurveyCollection() {
        return importSurveyCollection;
    }

    public void setImportSurveyCollection(Collection<ImportSurvey> importSurveyCollection) {
        this.importSurveyCollection = importSurveyCollection;
    }

    public ImportDataset getImportDataset() {
        return importDataset;
    }

    public void setImportDataset(ImportDataset importDataset) {
        this.importDataset = importDataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportTaxonDataset)) {
            return false;
        }
        ImportTaxonDataset other = (ImportTaxonDataset) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportTaxonDataset[ datasetKey=" + datasetKey + " ]";
    }
    
}
