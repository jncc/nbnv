
package nbn.webmapping.json.picker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nbn.common.database.DataAccessObject;
import nbn.common.taxon.TaxonObservation;

public abstract class DAOUtilisingPickerHandler<T extends TaxonObservation> implements PickerServletHandler<T> {
    private List<DataAccessObject> daosSuccessfullyCreated = new ArrayList<DataAccessObject>();
    
    protected abstract void constructRequiredDAOsAndAddThemToList(List<DataAccessObject> listToAddDAOsTo) throws SQLException;
    
    public final void construct() throws SQLException {
        try {
            constructRequiredDAOsAndAddThemToList(daosSuccessfullyCreated);
        }
        catch(SQLException sqlEx) {
            dispose();
            throw sqlEx;
        }
    }

    public final void dispose() {
        for(DataAccessObject currDAO : daosSuccessfullyCreated)
            currDAO.dispose();
    }
}
