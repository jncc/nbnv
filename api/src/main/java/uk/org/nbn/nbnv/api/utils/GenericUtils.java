/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 *
 * @author Matt Debont
 */
public class GenericUtils {
    protected static final String VERIFIED = "VERIFIED";
    protected static final String INCORRECT = "INCORRECT";
    protected static final String UNCERTAIN = "UNCERTAIN";
    protected static final String UNVERIFIED = "UNVERIFIED";
    
    protected static final Integer VERIFIED_ID = 1;
    protected static final Integer INCORRECT_ID = 2;
    protected static final Integer UNCERTAIN_ID = 3;
    protected static final Integer UNVERIFIED_ID = 4;
    
    /**
     * Returns a list of integers representing the ID's of the supplied list of 
     * verification statuses (supplied as a comma delimited string)
     * 
     * @param statuses A list of verification statuses for taxon observations,
     * includes VERIFIED, INCORRECT, UNCERTAIN and UNVERIFIED
     * 
     * @throws IllegalArgumentException If any string in the input list are not
     * a valid record verification status
     */
    protected List<Integer> getVerificationIDs (String input) throws IllegalArgumentException  {
        return getVerificationIDs(new ArrayList<String>(StringUtils.commaDelimitedListToSet(input)));
    }        
    
    /**
     * Returns a list of integers representing the ID's of the supplied list of 
     * verification statuses
     * 
     * @param statuses A list of verification statuses for taxon observations,
     * includes VERIFIED, INCORRECT, UNCERTAIN and UNVERIFIED
     * 
     * @throws IllegalArgumentException If any string in the input list are not
     * a valid record verification status
     */    
    protected List<Integer> getVerificationIDs (List<String> input) throws IllegalArgumentException  {
        List<Integer> ret = new ArrayList<Integer>();
        
        for (String status : input) {
            if (status.equalsIgnoreCase(VERIFIED)) {
                ret.add(1);
            } else if (status.equalsIgnoreCase(INCORRECT)) {
                ret.add(2);
            } else if (status.equalsIgnoreCase(UNCERTAIN)) {
                ret.add(3);
            } else if (status.equalsIgnoreCase(UNVERIFIED)) {
                ret.add(4);
            } else {
                throw new IllegalArgumentException("A supplied record verification status is not valid ('" + status + "') - Expected VERIFIED, INCORRECT, UNCERTAIN or UNVERIFIED");
            }
        }
        return ret;        
    }       
    
    protected String getVerificationFromIDs (List<Integer> input) throws IllegalArgumentException {
        String ret = "";
        
        for (Integer status : input) {
            if (status == VERIFIED_ID) {
                ret += VERIFIED;
            } else if (status == INCORRECT_ID) {
                ret += INCORRECT;
            } else if (status == UNCERTAIN_ID) {
                ret += UNCERTAIN;
            } else if (status == UNVERIFIED_ID) {
                ret += UNVERIFIED;
            } else {
                throw new IllegalArgumentException("A supplied record verification status is not valid ('" + status + "') - Expected VERIFIED (1), INCORRECT (2), UNCERTAIN (3) or UNVERIFIED (4)");
            }
            
            ret += ", ";
        }
        
        return ret.substring(0, ret.length() - 2);
    }    
    
    protected String listToSQLString(List<?> input) {
        String test = "(" + StringUtils.collectionToCommaDelimitedString(input) + ")";
        return StringUtils.collectionToCommaDelimitedString(input);
    }
}
