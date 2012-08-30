/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.webmapping.json.bridge.DesignationToJSONObjectBridge;
import nbn.webmapping.json.entity.EntityResolvingException;
import nbn.webmapping.json.entity.ResolveableEntityResolver;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class DesignationResolveableEntityResolver implements ResolveableEntityResolver<JSONObject>{

    public JSONObject resolveEntity(String desig) throws EntityResolvingException {
        try {
            DesignationDAO dao = new DesignationDAO();
            try {
                DesignationToJSONObjectBridge taxonBridge = new DesignationToJSONObjectBridge();
                return taxonBridge.convert(dao.getDesignation(desig));
            }
            finally {
                dao.dispose();
            }
        }
        catch(SQLException sql) {
            throw new EntityResolvingException("An Sql Exception has occured whilst attempting to resolve a designation entity", sql);
        }
    }

}
