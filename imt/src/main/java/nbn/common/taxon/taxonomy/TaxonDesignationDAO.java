
package nbn.common.taxon.taxonomy;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.common.database.DataAccessObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 01-Oct-2010
* @description	    :-
*/
public class TaxonDesignationDAO extends DataAccessObject {
    private DesignationDAO designationDAO;
    public TaxonDesignationDAO() throws SQLException {
        super();
	designationDAO = new DesignationDAO();
    }

    public List<TaxonDesignation> getAllTaxonDesignations(int internalTaxonKey, boolean includeFormerDesignations) throws SQLException {
	List<TaxonDesignation> toReturn = new LinkedList<TaxonDesignation>();

        CallableStatement cs = _conn.prepareCall((includeFormerDesignations) ? "{call NBNGateway.dbo.usp_getAllTaxonDesignationsForATaxon(?)}" : "{call NBNGateway.dbo.usp_getCurrentTaxonDesignationsForATaxon(?)}");
        cs.setInt(1,internalTaxonKey);
	ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeTaxonDesignation(rs));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    private TaxonDesignation composeTaxonDesignation(ResultSet rs) throws SQLException {
	Designation taxonsDesignation = designationDAO.getDesignation(rs.getInt("designationKey"));	
	TaxonDesignation toReturn = new TaxonDesignation(taxonsDesignation,convert(rs.getDate("startDate")),rs.getString("source"));
	toReturn.setEndDate(convert(rs.getDate("endDate")));
	toReturn.setGeographicalConstraint(rs.getString("geographicalConstraint"));
	toReturn.setStatusConstraint(rs.getString("statusConstraint"));
	return toReturn;
    }

    private static GregorianCalendar convert(Date sqlDate) {
	if(sqlDate == null)
	    return null;
	else {
	    GregorianCalendar toReturn = new GregorianCalendar();
	    toReturn.setTime(sqlDate);
	    return toReturn;
	}
    }
    @Override
    public void dispose() {
	super.dispose();
	designationDAO.dispose();
    }
}
