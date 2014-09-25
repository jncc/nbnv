/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reseter.dao.providers;

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
}
