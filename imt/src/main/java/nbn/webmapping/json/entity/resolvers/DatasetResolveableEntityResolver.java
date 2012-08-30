/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.webmapping.json.bridge.TaxonDatasetToJSONObjectBridge;
import nbn.webmapping.json.entity.EntityResolvingException;
import nbn.webmapping.json.entity.ResolveableEntityResolver;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class DatasetResolveableEntityResolver implements ResolveableEntityResolver<JSONObject> {
    private static final boolean INCLUDE_METADATA_BY_DEFAULT = false;
    private boolean includeMetadata;
    public DatasetResolveableEntityResolver() {
        this(INCLUDE_METADATA_BY_DEFAULT);
    }

    public DatasetResolveableEntityResolver(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public JSONObject resolveEntity(String dataset) throws EntityResolvingException {
        try {
            DatasetDAO dao = new DatasetDAO();
            try {
                TaxonDatasetToJSONObjectBridge datasetBridge = new TaxonDatasetToJSONObjectBridge(includeMetadata);
                TaxonDataset toConvert = dao.getTaxonDataset(dataset);
                return datasetBridge.convert(toConvert);
            }
            finally {
                dao.dispose();
            }
        }
        catch(SQLException sql) {
            throw new EntityResolvingException("An Sql Exception has occured whilst attempting to resolve a dataset entity", sql);
        }
    }
}
