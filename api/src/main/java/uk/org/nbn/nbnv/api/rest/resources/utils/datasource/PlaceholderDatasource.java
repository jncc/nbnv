/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils.datasource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Matt Debont
 */
public class PlaceholderDatasource implements DataSource {

    private class DummyOutputStream
            extends OutputStream {

        @Override
        public void write(int b)
                throws IOException {
        }
    }

    public PlaceholderDatasource() {
        super();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new PlaceholderSQLConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new PlaceholderSQLConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return new PrintWriter(new DummyOutputStream());
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return true;
    }
}
