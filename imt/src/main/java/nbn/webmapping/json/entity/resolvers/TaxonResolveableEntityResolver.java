/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import nbn.common.taxon.TaxonDAO;
import nbn.webmapping.json.bridge.TaxonToJSONObjectBridge;
import nbn.webmapping.json.entity.EntityResolvingException;
import nbn.webmapping.json.entity.ResolveableEntityResolver;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class TaxonResolveableEntityResolver implements ResolveableEntityResolver<JSONObject> {

    public JSONObject resolveEntity(String species) throws EntityResolvingException {
        try {
            TaxonDAO dao = new TaxonDAO();
            try {
                TaxonToJSONObjectBridge taxonBridge = new TaxonToJSONObjectBridge();
                return taxonBridge.convert(dao.getTaxon(species));
            }
            finally{
                dao.dispose();
            }
        }
        catch(SQLException ex) {
            throw new EntityResolvingException("An Sql Exception has occured whilst attempting to resolve a species entity", ex);
        }
    }

}
