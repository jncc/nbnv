/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.DependentStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class AttributeConcatenation extends DependentStep {
    private Map<Integer, String> columnList = new HashMap<Integer, String>();

    public AttributeConcatenation() {
        super(DependentStep.ADD_COLUMNS ^ DependentStep.PERSIST ^ DependentStep.RUN_FIRST);
    }
    
    @Override
    public String getName() {
        return "Concatenate attributes";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.ATTRIBUTE) {
                columnList.put(cm.getColumnNumber(), cm.getColumnLabel());
            }
        }
        
        return this.peristanceCheck(columnList.size() > 0);
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        int highestColumn = -1;
        
        for (ColumnMapping cm : columns) {
            highestColumn = cm.getColumnNumber() > highestColumn ? cm.getColumnNumber() : highestColumn;
        }
        
        ColumnMapping col = new ColumnMapping(highestColumn + 1, "DynamicProperties", DarwinCoreField.DYNAMICPROPERTIES);
        columns.add(col);
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        JSONObject obj = new JSONObject();
        
        for (int c : columnList.keySet()) {
            try {
                obj.put(columnList.get(c), row.get(c));
            } catch (JSONException ex) {
                Logger.getLogger(AttributeConcatenation.class.getName()).log(Level.SEVERE, null, ex);
                throw new BadDataException(ex);
            }
        }
        
        row.add(obj.toString());
    }
    
}