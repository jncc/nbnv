/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.sql.Date;
//import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
public class TaxonDesignation {
    private Designation designation;
    private Date startDate;
    private Date endDate;
    private String source;
    private String statusConstraint;

    /**
     * @return the designation
     */
    public Designation getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the statusConstraint
     */
    public String getStatusConstraint() {
        return statusConstraint;
    }

    /**
     * @param statusConstraint the statusConstraint to set
     */
    public void setStatusConstraint(String statusConstraint) {
        this.statusConstraint = statusConstraint;
    }
    
}
