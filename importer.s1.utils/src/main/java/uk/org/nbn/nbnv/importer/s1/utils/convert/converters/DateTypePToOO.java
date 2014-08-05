/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.convert.converters;

import java.util.List;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class DateTypePToOO extends ConverterStep {
    private int dateTypeColumn;    
    
    public DateTypePToOO() {
        super(ConverterStep.MODIFY ^ ConverterStep.RUN_FIRST);
    }
    
    @Override
    public String getName() {
        return "Change P Date Type to OO";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.EVENTDATETYPECODE) {
                dateTypeColumn = cm.getColumnNumber();
                return true;
            }
        }
        return false;
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {        

    }

    @Override
    public void modifyRow(List<String> row) throws BadDataException {
        if (row.get(dateTypeColumn).trim().equals("P")) {
            row.set(dateTypeColumn, "OO");
        }
    }
}
