/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class DataAccessObject {
    protected Connection _conn;

    protected DataAccessObject() throws SQLException {
        this._conn = DatabaseConnectionFactory.getNBNGatewayConnection();
    }

    public void dispose() {
       DatabaseConnectionFactory.disposeConnection(_conn);
    }

    public boolean testDAO() {
        try {
            return this._conn.isValid(2);
        } catch (SQLException ex) {
            return false;
        }
    }
}
