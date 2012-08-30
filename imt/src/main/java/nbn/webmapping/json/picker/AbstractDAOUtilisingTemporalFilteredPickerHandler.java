
package nbn.webmapping.json.picker;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import nbn.common.feature.Feature;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonObservation;
import nbn.common.user.User;
import nbn.common.util.servlet.Parameter;

public abstract class AbstractDAOUtilisingTemporalFilteredPickerHandler<T extends TaxonObservation> extends DAOUtilisingPickerHandler<T> {
    private static final Parameter START_YEAR_PARAMETER = new Parameter("startyear");
    private static final Parameter END_YEAR_PARAMETER = new Parameter("endyear");
    
    protected abstract List<Taxon> getSpeciesFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException;
    protected abstract List<DatasetTaxonObservationListPair<T>> getDatasetRecordsFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException;

    @Override
    public final List<Taxon> getSpeciesFound(Feature site, User requestingUser, Map<Parameter, String> requestParameters) throws SQLException {
        return getSpeciesFound(site, requestingUser, getStartYearFromRequest(requestParameters), getEndYearFromRequest(requestParameters), requestParameters);
    }

    @Override
    public final List<DatasetTaxonObservationListPair<T>> getDatasetRecordsFound(Feature site, User requestingUser, Map<Parameter, String> requestParameters) throws SQLException {
        return getDatasetRecordsFound(site, requestingUser, getStartYearFromRequest(requestParameters), getEndYearFromRequest(requestParameters), requestParameters);
    }

    private static int getStartYearFromRequest(Map<Parameter, String> requestParameters) {
        return (requestParameters.containsKey(START_YEAR_PARAMETER)) ? Integer.parseInt(requestParameters.get(START_YEAR_PARAMETER)) : 1799;
    }

    private static int getEndYearFromRequest(Map<Parameter, String> requestParameters) {
        return (requestParameters.containsKey(END_YEAR_PARAMETER)) ? Integer.parseInt(requestParameters.get(END_YEAR_PARAMETER)) : Calendar.getInstance().get(Calendar.YEAR);
    }
}
