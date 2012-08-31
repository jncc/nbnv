package nbn.common.organisation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import nbn.common.database.DataAccessObject;
import nbn.common.dataset.Dataset;
import nbn.common.util.Pair;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 10-Jan-2011
 * @description	    :-
 */
public class OrganisationDAO extends DataAccessObject {

    public OrganisationDAO() throws SQLException {
        super();
    }

    public Organisation getOrganisationByOrganisationKey(int organisationKey) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getOrganisation(?,?)}");
        cs.setInt(1, organisationKey);
        cs.setInt(2, 0);
        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeOrganisation(organisationKey, rs);
            } else {
                throw new IllegalArgumentException("The Organisation " + organisationKey + " was requested but does not exist");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<Organisation> getOrganisationsWithSpeciesDatasets() throws SQLException {
        List<Organisation> organisations = new ArrayList<Organisation>();
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getOrganisationsWithTaxonDatasets}");
        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                organisations.add(composeOrganisation(rs.getInt("organisationKey"), rs));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return organisations;
    }

    public <T extends Dataset> List<Pair<Organisation,List<T>>> getProvidingOrganisationsForDatasets(List<T> datasets) throws SQLException {
        Map<Integer,Pair<Organisation,List<T>>> visitedOrganisationKeys = new HashMap<Integer,Pair<Organisation,List<T>>>();

        for(T currDataset : datasets) {//going to pair all the datasets to there given organisation and then return as a list of pairs
            int currOrganisationKey = currDataset.getDatasetProvider().getOrganisationKey();
            List<T> toAddTo;
            if(!visitedOrganisationKeys.containsKey(currOrganisationKey)) {
                toAddTo = new ArrayList<T>();
                visitedOrganisationKeys.put(currOrganisationKey, new Pair<Organisation,List<T>>(getOrganisationByOrganisationKey(currOrganisationKey),toAddTo));
            }
            else
                toAddTo = visitedOrganisationKeys.get(currOrganisationKey).getB();
            toAddTo.add(currDataset);
        }
        return new ArrayList<Pair<Organisation,List<T>>>(visitedOrganisationKeys.values());
    }

    private Organisation composeOrganisation(int organisationKey, ResultSet rs) throws SQLException {
        Organisation toReturn = new Organisation(organisationKey);
        toReturn.setAbbreviation(rs.getString("abbreviation"));
        toReturn.setAddress(rs.getString("address"));
        toReturn.setAllowPublicRegistration(rs.getBoolean("allowPublicRegistration"));
        toReturn.setContactEmail(rs.getString("contactEmail"));
        toReturn.setContactName(rs.getString("contactName"));
        toReturn.setEmailAlertType(rs.getInt("emailAlertType"));
        toReturn.setOrganisationName(rs.getString("organisationName"));
        toReturn.setPostcode(rs.getString("postcode"));
        toReturn.setSummary(rs.getString("summary"));
        toReturn.setLogoURL(rs.getString("logoURL"));
        toReturn.setWebsite(rs.getString("website"));
        return toReturn;
    }
}
