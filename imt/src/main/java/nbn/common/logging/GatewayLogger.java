/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.logging;

import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import nbn.common.database.DatabaseConnectionFactory;

/**
 *
 * @author Administrator
 */
public class GatewayLogger {
    private static int logRequestMetadata(RequestParameters reqParameters) throws SQLException {
        Connection conn = getGatewayLogsConnection();

        try {
            Statement s = conn.createStatement();
            s.executeUpdate("SET XACT_ABORT ON");
            s.close();

            CallableStatement cs = conn.prepareCall("{call GATEWAY_LOGS.dbo.usp_insertRequest(?,?,?,?,?,?)}");
            cs.setInt(1, reqParameters.getMetadata().getUser().getUserKey());
            cs.setString(2, reqParameters.getWebservice());
            cs.setString(3, reqParameters.getMetadata().getReferer());
            cs.setString(4, reqParameters.getMetadata().getUserAgent());
            cs.setString(5, reqParameters.getMetadata().getRemoteHost());

            cs.registerOutParameter(6, Types.INTEGER);
            cs.executeUpdate();

            int requestId = cs.getInt(6);

            cs.close();
            return requestId;
        } finally {
            disposeConnection(conn);
        }
    }

    public static void log(RequestParameters requestParameters) throws SQLException {
        int requestId = logRequestMetadata(requestParameters);
        Connection conn = getGatewayLogsConnection();

        try {
            Statement s = conn.createStatement();
            s.executeUpdate("SET XACT_ABORT ON");
            s.close();

            CallableStatement cs = conn.prepareCall("{call GATEWAY_LOGS.dbo.usp_insertRequestParameter(?,?,?)}");

            for (RequestParameter rp : requestParameters.getParameters()) {
                logParameter(requestId, rp.getKey(), rp.getValue(), cs);
            }

            for (String dsKey : requestParameters.getDatasets()) {
                logParameter(requestId, "DATASET_KEY", dsKey, cs);
            }


            cs.clearParameters();
            cs = conn.prepareCall("{call GATEWAY_LOGS.dbo.usp_logSuccessful(?)}");
            cs.setInt(1, requestId);
            cs.executeUpdate();
        } finally {
            disposeConnection(conn);
        }

    }

    private static void logParameter(int requestId, String key, String value, CallableStatement cs) throws SQLException {
        cs.setInt(1,requestId);
        cs.setString(2, key);
        cs.setString(3, value);
        cs.executeUpdate();
    }
    private static Connection getGatewayLogsConnection() throws SQLException {
        return DatabaseConnectionFactory.getNBNGatewayConnection();
    }

    private static void disposeConnection(Connection conn) {
        if (conn != null)
            DatabaseConnectionFactory.disposeConnection(conn);
    }
}
