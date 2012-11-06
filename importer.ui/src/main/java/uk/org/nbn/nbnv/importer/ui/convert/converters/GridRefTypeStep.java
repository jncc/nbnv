/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Matt Debont
 */
public class GridRefTypeStep extends ConverterStep {
    private int gridRefTypeCol = -1;
    private int verbatimSRSCol = -1;
    private boolean verbatimSRSAdd = false;
    private static final Map<String, String> fieldMappings;
    
    static {
        fieldMappings = new HashMap<String, String>();
        
        fieldMappings.put("WGS84", "4326");
    }
    
    public GridRefTypeStep() {
        super(ConverterStep.ADD_COLUMN ^ ConverterStep.MODIFY);
    }

    @Override
    public String getName() {
        return "Check Grid Reference Projection for non Grid Reference Values";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        boolean foundType = false;

        for (ColumnMapping column : columns) {
            if (column.getField() == DarwinCoreField.GRIDREFERENCETYPE) {
                foundType = true;
                gridRefTypeCol = column.getColumnNumber();
            }
            if (column.getField() == DarwinCoreField.VERBATIMSRS) {
                verbatimSRSCol = column.getColumnNumber();
            }
        }
        
        if (foundType) {
            return true;
        }
        
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        if (verbatimSRSCol == -1) {
            for (ColumnMapping cm : columns) {
                verbatimSRSCol = cm.getColumnNumber() > verbatimSRSCol ? cm.getColumnNumber() : verbatimSRSCol;
            }

            verbatimSRSCol++;
            
            ColumnMapping cm = new ColumnMapping(verbatimSRSCol, "SRS", DarwinCoreField.VERBATIMSRS);
            columns.add(cm);
            verbatimSRSAdd = true;
        }        
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        String input = row.get(gridRefTypeCol).toUpperCase();
        
        if (fieldMappings.containsKey(input)) {
            if (verbatimSRSAdd) {
                row.add(fieldMappings.get(input));
            } else {
                row.set(verbatimSRSCol, fieldMappings.get(input));
            }
            
            row.set(gridRefTypeCol, "");
        } else {
            if (verbatimSRSAdd) {
                row.add("");
            }
        }
    }
    
}
