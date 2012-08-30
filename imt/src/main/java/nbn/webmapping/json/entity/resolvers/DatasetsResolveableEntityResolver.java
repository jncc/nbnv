/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.entity.resolvers;

import java.sql.SQLException;
import java.util.List;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import nbn.webmapping.json.bridge.TaxonDatasetToJSONObjectBridge;
import nbn.webmapping.json.entity.AbstractMultipleResolveableEntityResolver;
import nbn.webmapping.json.entity.EntityResolvingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class DatasetsResolveableEntityResolver extends AbstractMultipleResolveableEntityResolver {
    private static final boolean INCLUDE_METADATA_BY_DEFAULT = false;
    private boolean includeMetadata;
    public DatasetsResolveableEntityResolver() {
        this(INCLUDE_METADATA_BY_DEFAULT);
    }

    public DatasetsResolveableEntityResolver(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public @Override JSONArray resolveEntity(List<String> datasets) throws EntityResolvingException {
        try {
            DatasetDAO dao = new DatasetDAO();
            try {
                TaxonDatasetToJSONObjectBridge datasetBridge = new TaxonDatasetToJSONObjectBridge(includeMetadata);
                List<TaxonDataset> toConvert = dao.getTaxonDatasetListByDatasetKeys(datasets);
                return new JSONObjectListToJSONArrayBridge().convert(new ListBridge<TaxonDataset,JSONObject>(datasetBridge).convert(toConvert));
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
