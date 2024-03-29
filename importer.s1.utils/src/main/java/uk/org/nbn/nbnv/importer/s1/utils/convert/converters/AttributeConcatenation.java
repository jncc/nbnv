package uk.org.nbn.nbnv.importer.s1.utils.convert.converters;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreFieldType;

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
        return "Concatenate attributes, forced ascii, removed unicode characters and trimmed to 255";
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
                String attrib = Normalizer.normalize(row.get(c).trim(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("[^\\p{ASCII}]", "")
                        .replaceAll("^\"|\"$", "");
                obj.put(columnList.get(c), attrib.substring(0, Math.min(attrib.length(), 255)));
            } catch (JSONException ex) {
                Logger.getLogger(AttributeConcatenation.class.getName()).log(Level.SEVERE, null, ex);
                throw new BadDataException("Unable to process Record Attributes");
            }
        }
        
        row.add(obj.toString());
    }
}