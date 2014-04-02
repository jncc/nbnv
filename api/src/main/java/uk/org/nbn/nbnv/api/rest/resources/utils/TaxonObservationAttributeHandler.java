/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import uk.org.nbn.nbnv.api.model.TaxonObservationAttribute;

/**
 *
 * @author Matt Debont
 */
public class TaxonObservationAttributeHandler implements ResultHandler {

    private Map<Integer, Map<Integer, String>> observationAttributes;
    private Set<Integer> attributeIDs;
    
    public TaxonObservationAttributeHandler() {
        observationAttributes = new HashMap<Integer, Map<Integer, String>>();
        attributeIDs = new HashSet<Integer>();
    }
    
    public Map<Integer, Map<Integer, String>> getObservationAttributes() {
        return observationAttributes;
    }
    
    public List<Integer> getAttributeIDs() {
        return new ArrayList<Integer>(attributeIDs);
    }
    
    @Override
    public void handleResult(ResultContext rc) {
        TaxonObservationAttribute attribute = (TaxonObservationAttribute) rc.getResultObject();
        
        attributeIDs.add(attribute.getAttributeID());
        
        // Put attributes into a map structure for retrieval later
        // Map --> obsID --> Map --> attID --> attVal
        Map<Integer, String> temp = observationAttributes.get(attribute.getObservationID());

        if (temp == null) {
            temp = new HashMap<Integer, String>();
            observationAttributes.put(attribute.getObservationID(), temp);
        }

        temp.put(attribute.getAttributeID(), attribute.getTextValue());
    }
}
