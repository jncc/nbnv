
package nbn.common.taxon.taxonomy;

import java.util.GregorianCalendar;
import nbn.common.taxon.designation.Designation;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 01-Oct-2010
* @description	    :-
*/
public class TaxonDesignation {
    private Designation designation;
    private GregorianCalendar startDate, endDate;
    private String source, statusConstraint,geographicalConstraint;

    TaxonDesignation(Designation designation, GregorianCalendar startDate, String source) {
	this.designation = designation;
	this.startDate = startDate;
	this.source = source;
    }

    void setEndDate(GregorianCalendar endDate) {
	this.endDate = endDate;
    }

    void setStatusConstraint(String statusConstraint) {
	this.statusConstraint = statusConstraint;
    }

    void setGeographicalConstraint(String geographicalConstraint) {
	this.geographicalConstraint = geographicalConstraint;
    }

    public Designation getDesignation() {
	return designation;
    }

    public GregorianCalendar getEndDate() {
	return endDate;
    }

    public String getGeographicalConstraint() {
	return geographicalConstraint;
    }

    public String getSource() {
	return source;
    }

    public GregorianCalendar getStartDate() {
	return startDate;
    }

    public String getStatusConstraint() {
	return statusConstraint;
    }
}
