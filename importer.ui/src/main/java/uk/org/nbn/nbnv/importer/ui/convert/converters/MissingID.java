/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class MissingID extends ConverterStep {
    private int count = 1;
    
    public MissingID() {
        super(ConverterStep.ADD_COLUMN);
    }
    
    @Override
    public String getName() {
        return "Adding missing record key column with number sequence";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.OCCURRENCESTATUS) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
//        for (ColumnMapping cm : columns) {
//            cm.setColumnNumber(cm.getColumnNumber() + 1); // move all columns up 1
//        }

        ColumnMapping cm = new ColumnMapping(columns.size(), "OccurrenceID", DarwinCoreField.OCCURRENCEID);
        columns.add(cm);
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        row.add(Integer.toString(count));
        count++;
    }
    
    @Override
    public void checkMappings(List<ColumnMapping> mappings) {
        // No mappings need to be checked, we only insert a column
    }
}
