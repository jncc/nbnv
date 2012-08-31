package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import java.util.List;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.HabitatDataset;
import nbn.webmapping.json.bridge.GISLayeredDatasetToJSONObjectBridge;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import nbn.webmapping.json.entity.AbstractMultipleResolveableEntityResolver;
import nbn.webmapping.json.entity.EntityResolvingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class HabitatsResolveableEntityResolver extends AbstractMultipleResolveableEntityResolver {
    public @Override JSONArray resolveEntity(List<String> habitatDatasetKey) throws EntityResolvingException {
        try {
            DatasetDAO dao = new DatasetDAO();
            try {
                GISLayeredDatasetToJSONObjectBridge<HabitatDataset> habitatBridge = new GISLayeredDatasetToJSONObjectBridge<HabitatDataset>();
                List<HabitatDataset> toConvert = dao.getHabitatDatasets(habitatDatasetKey);
                return new JSONObjectListToJSONArrayBridge().convert(new ListBridge<HabitatDataset,JSONObject>(habitatBridge).convert(toConvert));
            }
            finally {
                dao.dispose();
            }
        }
        catch(SQLException sql) {
            throw new EntityResolvingException("An Sql Exception has occured whilst attempting to resolve a habitat entity", sql);
        }
    }
}
