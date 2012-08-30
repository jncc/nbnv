/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.entity.resolvers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import nbn.webmapping.json.entity.EntityResolvingException;
import nbn.webmapping.json.entity.ResolveableEntityResolver;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Administrator
 */
public class TaxonResolveableEntityResolver implements ResolveableEntityResolver<JSONObject> {
    private static String taxonService = "http://staging.testnbn.net/api/taxa/"; //TODO move to properties file
    public JSONObject resolveEntity(String species) throws EntityResolvingException {
        try {
            InputStream in = new URL(taxonService + species ).openStream();
            try {
                return new JSONObject(new JSONTokener(new InputStreamReader(in)));
            }
            finally {
                in.close();
            }
        }
        catch(IOException io) {
            throw new EntityResolvingException(io);
        }
        catch (JSONException ex) {
            throw new EntityResolvingException(ex);
        }
    }

}
