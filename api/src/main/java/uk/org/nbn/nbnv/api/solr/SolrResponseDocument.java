package uk.org.nbn.nbnv.api.solr;

import org.codehaus.jackson.annotate.JsonUnwrapped;

/**
 * The following class will wrap the result of a Solr Search result with type and
 * title properties
 * @author Christopher Johnson
 */
public class SolrResponseDocument<T> {
    private String entityType;
    private String searchMatchTitle;
    private String descript;
    private String extendedName;
    
    @JsonUnwrapped
    private T result;
    
    public SolrResponseDocument(T result, String entityType, String searchMatchTitle, String descript, String extendedName) {
        this.result = result;
        this.entityType = entityType;
        this.searchMatchTitle = searchMatchTitle;
        this.descript = descript;
        this.extendedName = extendedName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getSearchMatchTitle() {
        return searchMatchTitle;
    }

    public void setSearchMatchTitle(String searchMatchTitle) {
        this.searchMatchTitle = searchMatchTitle;
    }

    /**
     * @return the descript
     */
    public String getDescript() {
        return descript;
    }

    /**
     * @param descript the descript to set
     */
    public void setDescript(String descript) {
        this.descript = descript;
    }

    /**
     * @return the extendedName
     */
    public String getExtendedName() {
        return extendedName;
    }

    /**
     * @param extendedName the extendedName to set
     */
    public void setExtendedName(String extendedName) {
        this.extendedName = extendedName;
    }
}
