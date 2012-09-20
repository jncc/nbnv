/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.DependentStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class MissingID extends DependentStep {
    private int count = 1;
    
    public MissingID() {
        super(DependentStep.INSERT_COLUMN);
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
        for (ColumnMapping cm : columns) {
            cm.setColumnNumber(cm.getColumnNumber() + 1); // move all columns up 1
        }

        ColumnMapping cm = new ColumnMapping(0, "OccurrenceID", DarwinCoreField.OCCURRENCEID);
        columns.add(0, cm);
    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        row.add(0, Integer.toString(count));
        count++;
    }
    
}
