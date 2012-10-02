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
import javax.validation.constraints.Size;

/**
 *
 * @author Paul Gilbertson
 */
@Embeddable
public class DatasetKeywordPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "thesaurus")
    private String thesaurus;

    public DatasetKeywordPK() {
    }

    public DatasetKeywordPK(String datasetKey, String keyword, String thesaurus) {
        this.datasetKey = datasetKey;
        this.keyword = keyword;
        this.thesaurus = thesaurus;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getThesaurus() {
        return thesaurus;
    }

    public void setThesaurus(String thesaurus) {
        this.thesaurus = thesaurus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        hash += (keyword != null ? keyword.hashCode() : 0);
        hash += (thesaurus != null ? thesaurus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetKeywordPK)) {
            return false;
        }
        DatasetKeywordPK other = (DatasetKeywordPK) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        if ((this.keyword == null && other.keyword != null) || (this.keyword != null && !this.keyword.equals(other.keyword))) {
            return false;
        }
        if ((this.thesaurus == null && other.thesaurus != null) || (this.thesaurus != null && !this.thesaurus.equals(other.thesaurus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DatasetKeywordPK[ datasetKey=" + datasetKey + ", keyword=" + keyword + ", thesaurus=" + thesaurus + " ]";
    }
    
}
