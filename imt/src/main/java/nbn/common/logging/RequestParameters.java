/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.logging;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class RequestParameters {
    private List<RequestParameter> _parameters;
    private RequestMetadata _metadata;
    private String _webservice;
    private List<String> _datasets;

    public RequestParameters() {
	_parameters = new ArrayList<RequestParameter>();
	_datasets = new ArrayList<String>();
    }
    /**
     * @return the _parameters
     */
    public List<RequestParameter> getParameters() {
        return _parameters;
    }

    /**
     * @param parameters the _parameters to set
     */
    public void setParameters(List<RequestParameter> parameters) {
        this._parameters = parameters;
    }

    /**
     * @return the _metadata
     */
    public RequestMetadata getMetadata() {
        return _metadata;
    }

    /**
     * @param metadata the _metadata to set
     */
    public void setMetadata(RequestMetadata metadata) {
        this._metadata = metadata;
    }

    public void addParameter(RequestParameter parameter) {
        this._parameters.add(parameter);
    }

    public void addParameter(String key, String value) {
        this._parameters.add(new RequestParameter(key, value));
    }

    /**
     * @return the _webservice
     */
    public String getWebservice() {
        return _webservice;
    }

    /**
     * @param webservice the _webservice to set
     */
    public void setWebservice(String webservice) {
        this._webservice = webservice;
    }

    /**
     * @return the _datasets
     */
    public List<String> getDatasets() {
        return _datasets;
    }

    /**
     * @param datasets the _datasets to set
     */
    public void setDatasets(List<String> datasets) {
        this._datasets = datasets;
    }

}
