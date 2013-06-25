/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "DatasetKeyword")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetKeyword.findAll", query = "SELECT d FROM DatasetKeyword d"),
    @NamedQuery(name = "DatasetKeyword.findByDatasetKey", query = "SELECT d FROM DatasetKeyword d WHERE d.datasetKeywordPK.datasetKey = :datasetKey"),
    @NamedQuery(name = "DatasetKeyword.findByKeyword", query = "SELECT d FROM DatasetKeyword d WHERE d.datasetKeywordPK.keyword = :keyword"),
    @NamedQuery(name = "DatasetKeyword.findByThesaurus", query = "SELECT d FROM DatasetKeyword d WHERE d.datasetKeywordPK.thesaurus = :thesaurus")})
public class DatasetKeyword implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DatasetKeywordPK datasetKeywordPK;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Dataset dataset;

    public DatasetKeyword() {
    }

    public DatasetKeyword(DatasetKeywordPK datasetKeywordPK) {
        this.datasetKeywordPK = datasetKeywordPK;
    }

    public DatasetKeyword(String datasetKey, String keyword, String thesaurus) {
        this.datasetKeywordPK = new DatasetKeywordPK(datasetKey, keyword, thesaurus);
    }

    public DatasetKeywordPK getDatasetKeywordPK() {
        return datasetKeywordPK;
    }

    public void setDatasetKeywordPK(DatasetKeywordPK datasetKeywordPK) {
        this.datasetKeywordPK = datasetKeywordPK;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKeywordPK != null ? datasetKeywordPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetKeyword)) {
            return false;
        }
        DatasetKeyword other = (DatasetKeyword) object;
        if ((this.datasetKeywordPK == null && other.datasetKeywordPK != null) || (this.datasetKeywordPK != null && !this.datasetKeywordPK.equals(other.datasetKeywordPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DatasetKeyword[ datasetKeywordPK=" + datasetKeywordPK + " ]";
    }
    
}
