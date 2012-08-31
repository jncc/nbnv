/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.arcgis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import nbn.common.dataset.privileges.Privileges;
import nbn.common.dataset.privileges.PrivilegesDAO;
import nbn.common.logging.RequestMetadata;
import nbn.common.logging.RequestParameter;
import nbn.common.logging.RequestParameters;
import nbn.common.user.User;

/**
 *
 * @author Administrator
 */
public abstract class SpeciesDataRequest {
    protected int _startYear = 1799;
    protected int _endYear = Calendar.getInstance().get(Calendar.YEAR);
    protected User _user = User.PUBLIC_USER;
    protected List<String> _datasets;
    protected String _requestKey;
    protected List<Privileges> _privileges;

    public SpeciesDataRequest() {
        _datasets = new ArrayList<String>();
        _privileges = new ArrayList<Privileges>();
    }

    public void addDataset(String dataset) throws SQLException {
        _datasets.add(dataset);
        
        PrivilegesDAO dao = null;
        try {
            dao = new PrivilegesDAO();
            _privileges.add(dao.getPrivileges(_user, dataset));
        } finally {
            if (dao != null)
                dao.dispose();
        }

    }

    public void addDatasets(List<String> datasets) throws SQLException {
        for(String currDataset: datasets)
            addDataset(currDataset);
    }

    public List<String> getDatasets() {
        return _datasets;
    }

    /**
     * @return the _startYear
     */
    public int getStartYear() {
        return _startYear;
    }

    /**
     * @param startYear the _startYear to set
     */
    public void setStartYear(int startYear) {
        this._startYear = startYear;
    }

    /**
     * @return the _endYear
     */
    public int getEndYear() {
        return _endYear;
    }

    /**
     * @param endYear the _endYear to set
     */
    public void setEndYear(int endYear) {
        this._endYear = endYear;
    }

    /**
     * @return the _user
     */
    public User getUser() {
        return _user;
    }

    /**
     * @param user the _user to set
     */
    public void setUser(User user) throws SQLException {
        this._user = user;

        PrivilegesDAO dao = null;
        _privileges.clear();
        try {
            dao = new PrivilegesDAO();

            for (String dataset : _datasets)
                _privileges.add(dao.getPrivileges(_user, dataset));
        } finally {
            if (dao != null)
                dao.dispose();
        }
    }

    public String getRequestKey() {
        return _requestKey;
    }

    public List<Privileges> getPrivilegesList() {
        return _privileges;
    }

    protected abstract void appendExtraRequestParameters(RequestParameters out);

    public RequestParameters getRequestParameters(){
	RequestParameters requestParameters = new RequestParameters();
	requestParameters.setDatasets(getDatasets());
	requestParameters.addParameter("StartDate", String.valueOf(getStartYear()));
	requestParameters.addParameter("EndDate", String.valueOf(getEndYear()));
	requestParameters.setWebservice("IMT");
	requestParameters.setMetadata(getRequestMetadata());
	appendExtraRequestParameters(requestParameters);
	return requestParameters;
    }

    private RequestMetadata getRequestMetadata(){
	RequestMetadata requestMetadata = new RequestMetadata();
	requestMetadata.setReferer("http://data.nbn.org.uk/imt");
	requestMetadata.setRemoteHost("Not captured");
	requestMetadata.setUser(getUser());
	requestMetadata.setUserAgent("IMT");
	return requestMetadata;
    }

}
