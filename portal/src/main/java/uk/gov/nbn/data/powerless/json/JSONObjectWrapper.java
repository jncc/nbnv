/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.json;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Chris Johnson
 */
public class JSONObjectWrapper implements ObjectWrapper {
    private static final SimpleObjectWrapper SIMPLE_OBJECT_WRAPPER = new SimpleObjectWrapper();

    @Override
    public TemplateModel wrap(Object o) throws TemplateModelException {
        if (o instanceof JSONObject) {
            return new JSONTemplateHashModel((JSONObject) o, this);
        } else if (o instanceof JSONArray) {
            return new JSONArrayTemplateCollectionModel((JSONArray) o, this);
        } else if (JSONObject.NULL.equals(o)){
            return TemplateModel.NOTHING;
        } else {
            return SIMPLE_OBJECT_WRAPPER.wrap(o);
        }
    }
}
