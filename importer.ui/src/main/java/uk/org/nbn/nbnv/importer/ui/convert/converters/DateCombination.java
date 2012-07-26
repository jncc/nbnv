/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.List;
import java.util.Map;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class DateCombination implements ConverterStep {

    @Override
    public String getName() {
        return "Convert date format to ISO format";
    }

    @Override
    public boolean isStepNeeded(List<ColumnMapping> columns) {
        boolean startDate = false;
        boolean endDate = false;
        boolean dateType = false;
        
        for (ColumnMapping cm : columns) {
            if (cm.getField() == DarwinCoreField.STARTDATE) {
                startDate = true;
            }
            
            if (cm.getField() == DarwinCoreField.ENDDATE) {
                endDate = true;
            }
            
            if (cm.getField() == DarwinCoreField.EVENTDATETYPECODE) {
                dateType = true;
            }
        }
        
        if (startDate && endDate && dateType) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void modifyRow(Map<DarwinCoreField, String> row) throws BadDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modifyHeader(List<ColumnMapping> columns) {
        int highestNumber = -1;
        
        for (ColumnMapping col : columns) {
            highestNumber = col.getColumnNumber() > highestNumber ? col.getColumnNumber() : highestNumber;
        }
        
        ColumnMapping cm = new ColumnMapping(highestNumber + 1, "ISODate", DarwinCoreField.EVENTDATE);
        columns.add(cm);
    }
}
