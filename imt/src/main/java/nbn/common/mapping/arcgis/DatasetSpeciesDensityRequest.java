/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.arcgis;

import java.sql.SQLException;
import nbn.common.dataset.privileges.Privileges;
import nbn.common.dataset.privileges.PrivilegesDAO;
import nbn.common.logging.RequestParameters;
import nbn.common.user.User;

/**
 *
 * @author Administrator
 */
public final class DatasetSpeciesDensityRequest extends SpeciesDataRequest {
    private String _datasetKey;
    private Privileges _priv;

    public DatasetSpeciesDensityRequest(String dataset) throws SQLException {
        super();

        PrivilegesDAO dao = null;
        
        try {
            dao = new PrivilegesDAO();
            _datasetKey = dataset;
            _priv = dao.getPrivileges(_user, dataset);
        } finally {
            if (dao != null)
                dao.dispose();
        }
    }

    public String getDatasetKey() {
        return _datasetKey;
    }

    public Privileges getPrivileges() {
        return _priv;
    }

    @Override
    public void setUser(User user) {
        PrivilegesDAO dao = null;
        try {
            dao = new PrivilegesDAO();
            _priv = dao.getPrivileges(user, _datasetKey);
            super.setUser(user);
        } catch (SQLException ex) {
            //Logger.getLogger(DatasetSpeciesDensityRequest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (dao != null)
                dao.dispose();
        }
    }

    @Override
    protected void appendExtraRequestParameters(RequestParameters out) {
	out.addParameter("DATASET_KEY", getDatasetKey());
    }
}
