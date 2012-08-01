/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class AttributeConcatenation implements ConverterStep {

    @Override
    public String getName() {
        return "Concatenate attributes";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.ATTRIBUTE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        int highestColumn = -1;
        
        for (ColumnMapping cm : columns) {
            highestColumn = cm.getColumnNumber() > highestColumn ? cm.getColumnNumber() : highestColumn;
        }
        
        ColumnMapping col = new ColumnMapping(highestColumn + 1, "attributes", DarwinCoreField.DYNAMICPROPERTIES);
        columns.add(col);
    }

    @Override
    public void modifyRow(Map<DarwinCoreField, String> row) throws BadDataException {
        JSONObject obj = new JSONObject();
        
    }
    
}
