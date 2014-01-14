package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreFieldType;

/**
 *
 * @author Paul Gilbertson
 */
public class AttributeConcatenation extends ConverterStep {
    private Map<Integer, String> columnList;
    private int outColumn = -1;

    public AttributeConcatenation() {
        super(ConverterStep.ADD_COLUMN ^ ConverterStep.RUN_FIRST);
    }
    
    @Override
    public String getName() {
        return "Concatenate attributes";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        columnList = new HashMap<Integer, String>();
        
        // If we have an Attribute or a TaxonName (internal use fields)
        for (ColumnMapping cm : columns) {
            if (cm.getField().getType() == DarwinCoreFieldType.INTERNAL) {
                columnList.put(cm.getColumnNumber(), cm.getColumnLabel());
            }
        }
        
        return columnList.size() > 0;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {       
        for (ColumnMapping cm : columns) {
            outColumn = cm.getColumnNumber() > outColumn ? cm.getColumnNumber() : outColumn;
        }
        
        outColumn++;
        
        ColumnMapping col = new ColumnMapping(outColumn, "DynamicProperties", DarwinCoreField.DYNAMICPROPERTIES);
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
        
        row.add("\"" + obj.toString().replace("\"", "\"\"") + "\"");
    }
}