/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.json;

import freemarker.template.*;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.nbn.data.powerless.WrappedTemplateMethod;

/**
 *
 * @author Chris Johnson
 */
public class JSONTemplateHashModel extends WrappedTemplateMethod<JSONObject> implements TemplateHashModelEx {
    private final JSONObject toWrap;
    private final JSONObjectWrapper wrapper;

    public JSONTemplateHashModel(JSONObject toWrap, JSONObjectWrapper wrapper) {
        super(toWrap);
        this.toWrap = toWrap;
        this.wrapper = wrapper;
    }

    @Override
    public int size() throws TemplateModelException {
        return toWrap.length();
    }

    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        return new JSONArrayTemplateCollectionModel(toWrap.names(), wrapper);
    }

    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        return new SimpleCollection(toWrap.keys(), wrapper);
    }

    @Override
    public TemplateModel get(String param) throws TemplateModelException {
        try {
            return (toWrap.has(param)) ? wrapper.wrap(toWrap.get(param)) : null;
        } catch (JSONException jsonEx) {
            throw new TemplateModelException("A JSON Exception occurred", jsonEx);
        }
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return toWrap.length() != 0;
    }
}
