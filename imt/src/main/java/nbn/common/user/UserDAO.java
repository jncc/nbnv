/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.user;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class UserDAO extends DataAccessObject {
    public UserDAO() throws SQLException {
        super();
    }

    public User getPublicUser() {
        return User.PUBLIC_USER;
    }

    public User loginWebservice(String registrationKey) throws SQLException {
        User user = null;

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_checkWebserviceRegistration(?)}");
        cs.setString(1, registrationKey);

        ResultSet rs = cs.executeQuery();

        if (rs.next()) {
            Integer userkey = rs.getInt("loginUser");
            if (rs.wasNull())
                user = User.PUBLIC_USER;
            else
                user = getUser(userkey);
        } else {
            throw new IllegalArgumentException("Unknown webservice key (registrationKey = " + registrationKey + ").");
        }

        return user;
    }

    public User loginWebservice(String registrationKey, String username, String passKey) throws SQLException {
        User user = null;

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_checkWebserviceRegistration(?)}");
        cs.setString(1, registrationKey);

        ResultSet rs = cs.executeQuery();

        if (rs.next()) {
            Integer userkey = rs.getInt("loginUser");
            if (rs.wasNull())
                user = loginCookiesUser(username, passKey);
            else
                throw new IllegalArgumentException("Webservice key does not allow login privileges (registrationKey = " + registrationKey + ").");
        } else {
            throw new IllegalArgumentException("Unknown webservice key (registrationKey = " + registrationKey + ").");
        }

        return user;
    }

    public User getUser(int userKey) throws SQLException {
        User user = null;

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUser(?)}");
        cs.setInt(1, userKey);

        ResultSet rs = cs.executeQuery();

        if (rs.next()) {
            user = new User(userKey, rs.getString("fullname"));
        } else {
            throw new IllegalArgumentException("Unknown user (userKey = " + String.valueOf(userKey) + ").");
        }

        return user;
    }

    public User loginCookiesUser(String username, String passKey) throws SQLException {
        User user = null;

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_loginCookiesUser(?,?,?)}");
        cs.setString(1, username);
        cs.setString(2, passKey);
        cs.registerOutParameter(3, Types.INTEGER);

        cs.executeUpdate();
        int userkey = cs.getInt(3);
        if (userkey != 0) {
            user = getUser(userkey);
        } else {
            throw new IllegalArgumentException("Unknown user or bad passkey (username = " + username + ").");
        }

        return user;
    }
}
