/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset.privileges;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nbn.common.dataset.TaxonDataset;
import nbn.common.user.User;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class PrivilegesDAO extends DataAccessObject {
    public PrivilegesDAO() throws SQLException {
        super();
    }

    public Privileges getPrivileges(User user, TaxonDataset dataset) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserTaxonDatasetAccess(?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset.getDatasetKey());
        ResultSet rs = cs.executeQuery();
        try {
	    if (rs.next())
		return new Privileges(user, BlurLevel.getBlurLevelByCode(rs.getShort("resolution")), rs.getBoolean("allowDownload"), rs.getBoolean("viewSensitive"), rs.getBoolean("viewRecorder"), rs.getBoolean("viewAttributes"));
	    else
		return new Privileges(user, BlurLevel.NO_ACCESS, false, false, false, false);
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public Privileges getPrivileges(User user, String dataset) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserTaxonDatasetAccess(?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset);
        ResultSet rs = cs.executeQuery();
        try {
	    if (rs.next()) 
		return new Privileges(user, BlurLevel.getBlurLevelByCode(rs.getShort("resolution")), rs.getBoolean("allowDownload"), rs.getBoolean("viewSensitive"), rs.getBoolean("viewRecorder"), rs.getBoolean("viewAttributes"));
	    else
		return new Privileges(user, BlurLevel.NO_ACCESS, false, false, false, false);
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }
}
