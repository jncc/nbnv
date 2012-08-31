package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.SiteBoundaryDataset;
import nbn.webmapping.json.bridge.GISLayeredDatasetToJSONObjectBridge;
import nbn.webmapping.json.entity.EntityResolvingException;
import nbn.webmapping.json.entity.ResolveableEntityResolver;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class SiteBoundaryResolveableEntityResolver implements ResolveableEntityResolver<JSONObject> {
    public JSONObject resolveEntity(String siteBoundaryDatasetKey) throws EntityResolvingException {
        try {
            DatasetDAO dao = new DatasetDAO();
            try {
                GISLayeredDatasetToJSONObjectBridge<SiteBoundaryDataset> datasetBridge = new GISLayeredDatasetToJSONObjectBridge<SiteBoundaryDataset>();
                SiteBoundaryDataset toConvert = dao.getSiteBoundaryDataset(siteBoundaryDatasetKey);
                return datasetBridge.convert(toConvert);
            }
            finally {
                dao.dispose();
            }
        }
        catch (SQLException ex) {
            throw new EntityResolvingException("An Sql Exception has occured whilst attempting to resolve a site boundary entity", ex);
        }
    }
}
