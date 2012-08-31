/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Factory class for creating and disposing of database connections. All functions are static.
 *
 * @author Paul Gilbertson
 * @version 1.0
 */
public class DatabaseConnectionFactory {
    /**
     * Returns a {@link java.sql.Connection} to the NBNGateway database. Please use {@link disposeConnection} to dispose of the connection.
     * @return Connection to the NBN Gateway Database
     * @throws java.sql.SQLException
     */
    public static Connection getNBNGatewayConnection() throws java.sql.SQLException {
        return getConnection("nbn.sql.liveDB");
    }

    /**
     * Returns a {@link java.sql.Connection} to the NBNMetadata database. Please use {@link disposeConnection} to dispose of the connection
     * @return Connection to the NBN Metadata Database
     * @throws java.sql.SQLException
     */
    public static Connection getNBNMetadataConnection() throws java.sql.SQLException {
        return getConnection("nbn.sql.metadataDB");
    }

    private static Connection getConnection(String database) throws java.sql.SQLException {
        DataSource ds;
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup(ResourceBundle.getBundle("nbnServerSpecificProperties").getString(database));
        } catch (NamingException e) {
            throw new SQLException(e);
        }

        Connection connection = ds.getConnection();
        return connection;
    }

    /**
     * Used to dispose of a database connection
     * @param c Connection object to close and dispose of
     */
    public static void disposeConnection(Connection c)
    {
        try {
            if (c != null) {
                if (!c.isClosed()) {
                    c.close();
                }
            }
        }
        catch (Exception e)
        {
            Logger.getLogger("nbn.logs.database").severe("DatabaseConnectionFactory.disposeConnection: " + e.getMessage());
        }
    }
}
