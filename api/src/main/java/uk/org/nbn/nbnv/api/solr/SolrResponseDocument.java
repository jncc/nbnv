package uk.org.nbn.nbnv.api.solr;

import org.codehaus.jackson.annotate.JsonUnwrapped;

/**
 * The following class will wrap the result of a Solr Search result with type and
 * title properties
 * @author Christopher Johnson
 */
public class SolrResponseDocument<T> {
    private String entityType;
    private String title;
    
    @JsonUnwrapped
    private T result;
    
    public SolrResponseDocument(T result, String entityType, String title) {
        this.result = result;
        this.entityType = entityType;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
