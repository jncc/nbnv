/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.org.nbn.nbnv.api.model.TaxonObservation;

/**
 *
 * @author Matt Debont
 */
public class TaxonObservationHandler implements ResultHandler {

    private PrintWriter out;
    private boolean includeAttributes;
    private boolean firstRecord;

    public TaxonObservationHandler(PrintWriter out, boolean includeAttributes) {
        this.out = out;
        this.includeAttributes = includeAttributes;
        this.firstRecord = true;
    }

    @Override
    public void handleResult(ResultContext context) {
        TaxonObservation observation = (TaxonObservation) context.getResultObject();
        ObjectMapper om = new ObjectMapper();
        // Stop nulls being pushed out to the json output
        om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        ObjectWriter ow = om.writer();

        if (includeAttributes && (observation.isFullVersion() || observation.isPublicAttribute())) {
            observation.setAttributes(new HashMap<String, String>());

            String[] attVals = org.apache.commons.lang.StringUtils.split(
                    org.springframework.util.StringUtils.hasText(observation.getAttrStr())
                    ? observation.getAttrStr() : "", "¦");
            for (String attVal : attVals) {
                if (org.springframework.util.StringUtils.hasText(attVal)) {
                    String[] vals = org.apache.commons.lang.StringUtils.split(attVal, "¬");
                    if (vals.length == 2) {
                        observation.getAttributes().put(vals[0], vals[1]);
                    } else if (vals.length == 1) {
                        observation.getAttributes().put(vals[0], "");
                    }
                }
            }
        }
        observation.setAttrStr(null);

        try {
            if (firstRecord) {
                out.print(ow.writeValueAsString(observation));
                firstRecord = false;
            } else {
                out.print("," + ow.writeValueAsString(observation));
            }
        } catch (IOException ex) {
            //TODO: DO SOMETHING
        }
    }
}
