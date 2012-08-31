/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.picker;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import nbn.common.feature.Feature;
import nbn.common.taxon.*;
import nbn.common.user.User;
import nbn.common.util.servlet.Parameter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public interface PickerServletHandler<T extends TaxonObservation> {
    public List<Taxon> getSpeciesFound(Feature site, User requestingUser, Map<Parameter, String> requestParameters) throws SQLException;
    public List<DatasetTaxonObservationListPair<T>> getDatasetRecordsFound(Feature site, User requestingUser, Map<Parameter, String> requestParameters) throws SQLException;
    public JSONObject getAdditional(Feature site, List<Taxon> foundSpecies, List<DatasetTaxonObservationListPair<T>> foundRecords) throws JSONException;
    public void construct() throws SQLException;
    public void dispose();
}
