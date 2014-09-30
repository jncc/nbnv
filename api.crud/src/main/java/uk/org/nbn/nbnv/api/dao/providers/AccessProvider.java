/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;
//import static org.apache.ibatis.jdbc.
/**
 *
 * @author paulbe
 */
public class AccessProvider {
    public String addUserAccess(Map<String, Object> params) {
        TaxonObservationProvider tobsp = new TaxonObservationProvider();
        String from = tobsp.createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("DISTINCT #{user.id}, obs.id");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM UserTaxonObservationAccess utoa WHERE utoa.userID = #{user.id} )");
        return "INSERT INTO UserTaxonObservationAccess (userID, observationID) " + SQL();
    }

    public String removeUserAccess(Map<String, Object> params) {
        TaxonObservationProvider tobsp = new TaxonObservationProvider();
        String from = tobsp.createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("obs.id");
        FROM(from);
        WHERE("obs.id IN ( SELECT utoa.observationID FROM UserTaxonObservationAccess utoa WHERE utoa.userID = #{user.id} )");
        return "DELETE FROM UserTaxonObservationAccess WHERE userID = #{user.id} AND observationID IN (" + SQL() + ")" ;
    }

    public String addOrgAccess(Map<String, Object> params) {
        TaxonObservationProvider tobsp = new TaxonObservationProvider();
        String from = tobsp.createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("DISTINCT #{organisation.id}, obs.id");
        FROM(from);
        WHERE("obs.id NOT IN ( SELECT utoa.observationID FROM OrganisationTaxonObservationID utoa WHERE utoa.organisationID = #{organisation.id} )");
        return "INSERT INTO OrganisationTaxonObservationAccess (organisationID, observationID) " + SQL();
    }

    public String removeOrgAccess(Map<String, Object> params) {
        TaxonObservationProvider tobsp = new TaxonObservationProvider();
        String from = tobsp.createSelectEnhanced(params, "o.id");
        BEGIN();
        SELECT("obs.id");
        FROM(from);
        WHERE("obs.id IN ( SELECT utoa.observationID FROM OrganisationTaxonObservationID utoa WHERE utoa.organisationID = #{organisation.id} )");
        return "DELETE FROM OrganisationTaxonObservationAccess WHERE organisationID = #{organisation.id} AND observationID IN (" + SQL() + ")" ;
    }
}
