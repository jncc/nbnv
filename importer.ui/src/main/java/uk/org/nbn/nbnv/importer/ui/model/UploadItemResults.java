/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Administrator
 */
public class UploadItemResults {
    private List<String> results;
    private List<ColumnMapping> headers;
    private List<DarwinCoreField> fields;

    /**
     * @return the results
     */
    public List<String> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<String> results) {
        this.results = results;
    }

    /**
     * @return the headers
     */
    public List<ColumnMapping> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(List<ColumnMapping> headers) {
        this.headers = headers;
    }

    /**
     * @return the fields
     */
    public List<DarwinCoreField> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<DarwinCoreField> fields) {
        this.fields = fields;
    }
}
